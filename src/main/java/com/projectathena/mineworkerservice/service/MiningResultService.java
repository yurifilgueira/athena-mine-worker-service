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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MiningResultService {

    private static final Logger logger = LoggerFactory.getLogger(MiningResultService.class);

    private final MiningResultRepository miningResultRepository;
    private final MiningCommitRepository miningCommitRepository;
    private final GitAuthorRepository gitAuthorRepository;

    public MiningResultService(MiningResultRepository miningResultRepository,
                               MiningCommitRepository miningCommitRepository,
                               GitAuthorRepository gitAuthorRepository) {
        this.miningResultRepository = miningResultRepository;
        this.miningCommitRepository = miningCommitRepository;
        this.gitAuthorRepository = gitAuthorRepository;
    }

    @Transactional
    public MiningResult startMiningResult(Job job) {
        logger.info("Iniciando MiningResult para job: {}", job.getId());

        MiningResult miningResult = new MiningResult();
        miningResult.setJobId(job.getId());
        miningResult.setRepositoryOwner(job.getGitRepositoryOwner());
        miningResult.setRepositoryName(job.getGitRepositoryName());
        miningResult.setStartedAt(LocalDateTime.now());
        miningResult.setLastUpdatedAt(LocalDateTime.now());
        miningResult.setStatus(MiningStatus.IN_PROGRESS);
        miningResult.setTotalCommits(0);

        return miningResultRepository.save(miningResult);
    }

    @Transactional
    public MiningResult saveCommitPage(Job job, List<Commit> commits, String cursor) {
        logger.info("Salving page with {} commits for job: {}", commits.size(), job.getId());

        MiningResult miningResult = miningResultRepository.findByJobId(job.getId())
                .orElseGet(() -> startMiningResult(job));

        int savedCount = 0;
        for (Commit commitDto : commits) {

            MiningCommit miningCommit = new MiningCommit();
            miningCommit.setMiningResult(miningResult);
            miningCommit.setOid(commitDto.oid());
            miningCommit.setMessage(commitDto.message());
            miningCommit.setMessageBody(commitDto.messageBody());
            miningCommit.setAdditions(commitDto.additions());
            miningCommit.setDeletions(commitDto.deletions());
            miningCommit.setAuthoredByCommitter(commitDto.authoredByCommitter());
            miningCommit.setCommitUrl(commitDto.commitUrl());
            miningCommit.setCommittedDate(LocalDateTime.ofInstant(commitDto.committedDate().toInstant(), ZoneId.systemDefault()));

            if (commitDto.author() != null) {
                GitAuthor author = findOrCreateGitAuthor(commitDto.author());
                miningCommit.setAuthor(author);
            }

            if (commitDto.committer() != null) {
                GitAuthor committer = findOrCreateGitAuthor(commitDto.committer());
                miningCommit.setCommitter(committer);
            }

            miningCommitRepository.save(miningCommit);
            savedCount++;
        }

        miningResult.setLastUpdatedAt(LocalDateTime.now());
        miningResult.setLastCursor(cursor);
        miningResult.setTotalCommits(miningResult.getTotalCommits() + savedCount);
        
        logger.info("Saved {} new commits. Cumulative total: {}", savedCount, miningResult.getTotalCommits());
        
        return miningResultRepository.save(miningResult);
    }

    @Transactional
    public MiningResult completeMiningResult(Job job) {
        logger.info("Finalizing MiningResult for job: {}", job.getId());

        MiningResult miningResult = miningResultRepository.findByJobId(job.getId())
                .orElseThrow(() -> new RuntimeException("MiningResult not found for job: " + job.getId()));

        miningResult.setStatus(MiningStatus.COMPLETED);
        miningResult.setLastUpdatedAt(LocalDateTime.now());

        return miningResultRepository.save(miningResult);
    }

    private GitAuthor findOrCreateGitAuthor(GitActor gitActor) {
        if (gitActor.user() != null && gitActor.user().id() != null) {
            var existingAuthor = gitAuthorRepository.findByUserId(gitActor.user().id());
            if (existingAuthor.isPresent()) {
                return existingAuthor.get();
            }
        }

        if (gitActor.email() != null) {
            var existingAuthor = gitAuthorRepository.findByEmail(gitActor.email());
            if (existingAuthor.isPresent()) {
                return existingAuthor.get();
            }
        }

        GitAuthor newAuthor = new GitAuthor();
        newAuthor.setAvatarUrl(gitActor.avatarUrl());
        LocalDateTime date = LocalDateTime.ofInstant(gitActor.date().toInstant(), ZoneId.systemDefault());
        newAuthor.setDate(date);
        newAuthor.setEmail(gitActor.email());
        newAuthor.setName(gitActor.name());

        if (gitActor.user() != null) {
            newAuthor.setUserId(gitActor.user().id());
            newAuthor.setLogin(gitActor.user().login());
        }

        return gitAuthorRepository.save(newAuthor);
    }

    public Optional<MiningResult> findForUserAndRepository(MiningResultRequest request) {
        return miningResultRepository.findForUserAndRepository(request.userEmail(), request.gitRepositoryOwner(), request.gitRepositoryName());
    }
}
