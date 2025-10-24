package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.configs.WorkerIdProvider;
import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
import com.projectathena.mineworkerservice.model.dto.responses.JobSubmissionResponse;
import com.projectathena.mineworkerservice.model.dto.responses.JobStatusResponse;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.entities.User;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import com.projectathena.mineworkerservice.repositories.JobRepository;
import com.projectathena.mineworkerservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final WorkerIdProvider workerIdProvider;
    private final Logger logger = LoggerFactory.getLogger(JobService.class);
    @Value(value = "${spring.application.name}")
    private String applicationName;
    private final static String BASE_URL_VERIFY_JOB_STATUS = "http://localhost:8080";
    private final TransactionalOperator transactionalOperator;

    public JobService(JobRepository jobRepository, WorkerIdProvider workerIdProvider, UserRepository userRepository, TransactionalOperator transactionalOperator) {
        this.jobRepository = jobRepository;
        this.workerIdProvider = workerIdProvider;
        this.userRepository = userRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<Job> findPendingJob() {
        return jobRepository.findFirstByJobStatusOrderByCreatedAtAsc(JobStatus.PENDING)
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> updateJobStatusToMining(Job job) {
        job.setJobStatus(JobStatus.MINING);
        job.setStartedAt(Instant.now());
        job.setLastUpdated(Instant.now());
        job.setWorkerId(workerIdProvider.getWorkerId());
        return jobRepository.save(job).then();
    }

    public Mono<Void> updateJobStatusToCompleted(Job job) {
        job.setJobStatus(JobStatus.COMPLETED);
        job.setFinishedAt(Instant.now());
        job.setLastUpdated(Instant.now());
        return jobRepository.save(job).then();
    }

    public Mono<Void> updateJobProgress(Job job, String cursor) {
        job.setLastUpdated(Instant.now());
        job.setCursor(cursor);
        return jobRepository.save(job).then();
    }

    public Mono<JobSubmissionResponse> publishJob(PublishJobRequest request) {
        Mono<User> userMono = userRepository.findByEmail(request.userEmail())
                .switchIfEmpty(Mono.defer(() -> {
                    User newUser = new User();
                    newUser.setName(request.userName());
                    newUser.setEmail(request.userEmail());
                    return userRepository.save(newUser);
                }));

        return userMono
                .flatMap(user -> {
                    return jobRepository.findByRequestedByIdAndGitRepositoryNameAndGitRepositoryOwner(
                            user.getId(),
                            request.gitRepositoryName(),
                            request.gitRepositoryOwner()
                    ).switchIfEmpty(
                            Mono.defer(() -> {
                                Job newJob = new Job();
                                newJob.setRequestedBy(user);
                                newJob.setRequestedById(user.getId());
                                newJob.setJobStatus(JobStatus.PENDING);
                                newJob.setCreatedAt(Instant.now());
                                newJob.setGitRepositoryOwner(request.gitRepositoryOwner());
                                newJob.setGitRepositoryName(request.gitRepositoryName());

                                return jobRepository.save(newJob);
                            })
                    );
                })
                .flatMap(j -> {
                    if (j.getCursor() != null) {
                        j.setJobStatus(JobStatus.PENDING);
                        return jobRepository.save(j);
                    }else {
                        return Mono.just(j);
                    }
                })
                .map(savedJob -> {
                    String urlJobStatus = BASE_URL_VERIFY_JOB_STATUS + "/" + applicationName + "/jobs/status/" + savedJob.getId();
                    return new JobSubmissionResponse(savedJob.getId(), savedJob.getJobStatus(), urlJobStatus);
                })
                .as(transactionalOperator::transactional);
    }

    public Flux<Job> findJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByJobStatus(jobStatus);
    }

    public Mono<Void> updateJobToPending(Job job) {
        job.setJobStatus(JobStatus.PENDING);
        job.setLastUpdated(Instant.now());
        job.setWorkerId(null);

        return jobRepository.save(job)
                .doOnSuccess(savedJob -> logger.info("Job updated to PENDING: {}", savedJob.getId()))
                .then();
    }

    public Mono<Job> findById(UUID id) {
        return jobRepository.findById(id);
    }

    public Mono<JobStatusResponse> findJobStatusById(UUID id) {
        return jobRepository.findById(id)
                .map(job -> new JobStatusResponse(job.getId(), job.getJobStatus()));
    }
}