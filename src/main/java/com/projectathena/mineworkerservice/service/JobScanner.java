package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.model.entities.Job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobScanner {

    private final JobService jobService;
    private final MineService mineService;
    private final ExecutorService executorService;

    public JobScanner(JobService jobService, MineService mineService) {
        this.jobService = jobService;
        this.mineService = mineService;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Scheduled(fixedRate = 5000)
    public void scan() {
        executorService.submit(() -> {
            Optional<Job> job = jobService.findPendingJob();
            job.ifPresent(value -> {
                jobService.updateJobStatusToMining(value);
                mineService.mineCommits(job.get());
                jobService.updateJobStatusToCompleted(value);
            });
        });
    }

}
