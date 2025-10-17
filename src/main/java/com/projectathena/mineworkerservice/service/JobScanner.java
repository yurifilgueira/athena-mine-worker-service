package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.model.entities.Job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobScanner {

    private final JobService jobService;
    private final MineService mineService;

    public JobScanner(JobService jobService, MineService mineService) {
        this.jobService = jobService;
        this.mineService = mineService;
    }

    @Scheduled(fixedRate = 1000)
    public void scan(){
        Optional<Job> job = jobService.findPendingJob();
        job.ifPresent(value -> {

            jobService.updateJobStatusToMining(value);
            String repoName = job.get().getGitRepositoryName();
            String repoOwner = job.get().getGitRepositoryOwner();
            mineService.mineCommits(repoName, repoOwner);
            jobService.updateJobStatusToCompleted(value);
        });
    }

}
