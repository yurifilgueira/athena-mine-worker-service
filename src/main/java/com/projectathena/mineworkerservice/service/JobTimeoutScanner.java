package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class JobTimeoutScanner {

    private final JobService jobService;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(JobTimeoutScanner.class);

    public JobTimeoutScanner(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void scan() {
        List<Job> jobs = jobService.findJobsByStatus(JobStatus.MINING);

        for (Job job : jobs) {
            Instant jobLastUpdatedAt = job.getLastUpdated().toInstant();
            Instant now = Instant.now();
            Duration duration = Duration.between(jobLastUpdatedAt, now);

            if (duration.toSeconds() > 60) {
                logger.warn("Job Timeout: {}", job.getId());
                logger.info("Job Update Status: {} to {}", job.getJobStatus(), JobStatus.PENDING);
                jobService.updateJobToPending(job);
            }
        }
    }

}
