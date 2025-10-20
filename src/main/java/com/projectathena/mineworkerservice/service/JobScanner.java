package com.projectathena.mineworkerservice.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Component
public class JobScanner {

    private final JobService jobService;
    private final MineService mineService;
    private final Logger logger = LoggerFactory.getLogger(JobScanner.class);

    public JobScanner(JobService jobService, MineService mineService) {
        this.jobService = jobService;
        this.mineService = mineService;
    }

    @PostConstruct
    public void startScanning() {
        Flux.interval(Duration.ofSeconds(5))
                .flatMap(tick -> processNextJob())
                .subscribe(null, error -> logger.error("An error occurred during job scanning.", error));
    }

    private Mono<Void> processNextJob() {
        return jobService.findPendingJob()
                .flatMap(job -> jobService.updateJobStatusToMining(job).thenReturn(job))
                .doOnNext(job -> logger.info("Initiating job: {}", job.getId()))
                .flatMap(job -> mineService.mineCommits(job).thenReturn(job))
                .doOnNext(job -> logger.info("Finishing job: {}", job.getId()))
                .flatMap(jobService::updateJobStatusToCompleted);
    }
}