package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

@Service
public class JobTimeoutScanner {

    private final JobService jobService;
    private final TransactionalOperator transactionalOperator;
    private final Logger logger = LoggerFactory.getLogger(JobTimeoutScanner.class);

    public JobTimeoutScanner(JobService jobService, TransactionalOperator transactionalOperator) {
        this.jobService = jobService;
        this.transactionalOperator = transactionalOperator;
    }

    @PostConstruct
    public void startScanning() {
        Flux.interval(Duration.ofSeconds(30))
                .flatMap(tick -> scanAndResetTimedOutJobs())
                .subscribe(
                        null,
                        error -> logger.error("An error occurred in the JobTimeoutScanner pipeline.", error)
                );
    }

    private Mono<Void> scanAndResetTimedOutJobs() {
        return jobService.findJobsByStatus(JobStatus.MINING)
                .filter(job -> {
                    if (job.getLastUpdated() == null) return false;
                    Instant jobLastUpdatedAt = job.getLastUpdated().toInstant(ZoneId.systemDefault().getRules()
                            .getOffset(job.getLastUpdated()));
                    Duration duration = Duration.between(jobLastUpdatedAt, Instant.now());
                    return duration.toSeconds() > 60;
                })
                .flatMap(this::resetJobToPending)
                .then();
    }


    private Mono<Void> resetJobToPending(Job job) {
        logger.warn("Job Timeout detected for job: {}", job.getId());
        logger.info("Resetting job status to PENDING");

        return jobService.updateJobToPending(job)
                .as(transactionalOperator::transactional);
    }
}