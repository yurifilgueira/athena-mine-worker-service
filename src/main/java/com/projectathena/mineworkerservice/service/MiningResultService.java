package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.dto.commit.GitActor;
import com.projectathena.mineworkerservice.model.dto.requests.MiningResultRequest;
import com.projectathena.mineworkerservice.model.entities.GitAuthor;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.entities.MiningCommit;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import com.projectathena.mineworkerservice.model.enums.MiningStatus;
import com.projectathena.mineworkerservice.repositories.GitAuthorRepository;
import com.projectathena.mineworkerservice.repositories.MiningCommitRepository;
import com.projectathena.mineworkerservice.repositories.MiningResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MiningResultService {

    private static final Logger logger = LoggerFactory.getLogger(MiningResultService.class);

    private final MiningResultRepository miningResultRepository;
    private final MiningCommitRepository miningCommitRepository;
    private final GitAuthorRepository gitAuthorRepository;
    private final TransactionalOperator transactionalOperator; // Necessário para transação reativa

    public MiningResultService(MiningResultRepository miningResultRepository,
                               MiningCommitRepository miningCommitRepository,
                               GitAuthorRepository gitAuthorRepository,
                               // Injetar o TransactionalOperator
                               TransactionalOperator transactionalOperator) {
        this.miningResultRepository = miningResultRepository;
        this.miningCommitRepository = miningCommitRepository;
        this.gitAuthorRepository = gitAuthorRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<MiningResult> startMiningResult(Job job) {
        logger.info("Iniciando MiningResult para job: {}", job.getId());

        MiningResult miningResult = new MiningResult();
        miningResult.setJobId(job.getId());
        miningResult.setRepositoryOwner(job.getGitRepositoryOwner());
        miningResult.setRepositoryName(job.getGitRepositoryName());

        miningResult.setStartedAt(Instant.now());
        miningResult.setLastUpdatedAt(Instant.now());
        miningResult.setStatus(MiningStatus.IN_PROGRESS);
        miningResult.setTotalCommits(0);

        return miningResultRepository.save(miningResult);
    }

    public Mono<Void> saveCommitPage(Job job, List<Commit> commits, String cursor) {
        Mono<MiningResult> miningResultMono = miningResultRepository.findByJobId(job.getId())
                .switchIfEmpty(this.startMiningResult(job));

        return miningResultMono
                .publishOn(Schedulers.boundedElastic())
                .flatMap(miningResult -> {
                    logger.info("Salving page with {} commits for job: {}", commits.size(), job.getId());
                    UUID miningResultId = Objects.requireNonNull(miningResult.getId(), "MiningResult ID cannot be null.");

                    Flux<MiningCommit> miningCommitsFlux = Flux.fromIterable(commits)
                            .flatMap(commitDto -> {
                                Mono<GitAuthor> authorMono = findOrCreateGitAuthor(commitDto.author());
                                Mono<GitAuthor> committerMono = findOrCreateGitAuthor(commitDto.committer());

                                return Mono.zip(authorMono, committerMono)
                                        .map(tuple -> {
                                            MiningCommit miningCommit = new MiningCommit();

                                            miningCommit.setMiningResultId(miningResultId);

                                            miningCommit.setAuthorId(tuple.getT1().getId());
                                            miningCommit.setCommitterId(tuple.getT2().getId());

                                            miningCommit.setOid(commitDto.oid());
                                            miningCommit.setMessage(commitDto.message());
                                            miningCommit.setMessageBody(commitDto.messageBody());
                                            miningCommit.setAdditions(commitDto.additions());
                                            miningCommit.setDeletions(commitDto.deletions());
                                            miningCommit.setAuthoredByCommitter(commitDto.authoredByCommitter());
                                            miningCommit.setCommitUrl(commitDto.commitUrl());
                                            miningCommit.setCommittedDate(commitDto.committedDate());

                                            miningCommit.setAuthor(tuple.getT1());
                                            miningCommit.setCommitter(tuple.getT2());
                                            return miningCommit;
                                        });
                            });

                    return miningCommitRepository.saveAll(miningCommitsFlux)
                            .then(Mono.defer(() -> {
                                miningResult.setLastUpdatedAt(Instant.now());
                                miningResult.setLastCursor(cursor);
                                miningResult.setTotalCommits(miningResult.getTotalCommits() + commits.size());
                                logger.info("Saved {} new commits. Cumulative total: {}", commits.size(), miningResult.getTotalCommits());

                                return miningResultRepository.save(miningResult);
                            }));
                })
                .as(transactionalOperator::transactional)
                .then();
    }

    public Mono<Void> completeMiningResult(Job job) {
        logger.info("Finalizando MiningResult para job: {}", job.getId());

        return miningResultRepository.findByJobId(job.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("MiningResult not found for job: " + job.getId())))
                .flatMap(miningResult -> {
                    miningResult.setStatus(MiningStatus.COMPLETED);
                    miningResult.setLastUpdatedAt(Instant.now());
                    return miningResultRepository.save(miningResult);
                })
                .as(transactionalOperator::transactional) // Aplica a transação a todo o fluxo
                .then();
    }

    private Mono<GitAuthor> findOrCreateGitAuthor(GitActor gitActor) {
        if (gitActor == null) {
            return Mono.empty();
        }

        Mono<GitAuthor> findByUserIdMono = Mono.justOrEmpty(gitActor.user())
                .filter(user -> isUuidString(user.id()))
                .map(user -> UUID.fromString(user.id()))
                .flatMap(gitAuthorRepository::findByUserId);

        Mono<GitAuthor> findByEmailMono = Mono.justOrEmpty(gitActor.email())
                .flatMap(gitAuthorRepository::findByEmail);

        return Mono.firstWithSignal(findByUserIdMono, findByEmailMono)
                .switchIfEmpty(Mono.defer(() -> {
                    GitAuthor newAuthor = new GitAuthor();
                    newAuthor.setAvatarUrl(gitActor.avatarUrl());
                    newAuthor.setDate(gitActor.date().toInstant());
                    newAuthor.setEmail(gitActor.email());
                    newAuthor.setName(gitActor.name());

                    if (gitActor.user() != null) {
                        if (isUuidString(gitActor.user().id())) {
                            newAuthor.setUserId(UUID.fromString(gitActor.user().id()));
                        }
                        newAuthor.setLogin(gitActor.user().login());
                    }

                    return gitAuthorRepository.save(newAuthor);
                }));
    }

    private boolean isUuidString(String str) {
        if (str == null) return false;
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Mono<MiningResult> findForUserAndRepository(MiningResultRequest request) {
        return miningResultRepository.findByJobUserAndRepository(request.userEmail(), request.gitRepositoryOwner(), request.gitRepositoryName());
    }
}