package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.configs.WorkerIdProvider;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import com.projectathena.mineworkerservice.repositories.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final WorkerIdProvider workerIdProvider;

    public JobService(JobRepository jobRepository, WorkerIdProvider workerIdProvider) {
        this.jobRepository = jobRepository;
        this.workerIdProvider = workerIdProvider;
    }

    public Optional<Job> findById(String id) {
        return jobRepository.findById(id);
    }

    @Transactional
    public Optional<Job> findPendingJob() {
        return jobRepository.findFirstByJobStatusOrderByCreatedAtAsc(JobStatus.PENDING);
    }

    public void updateJobStatusToMining(Job job) {
        job.setJobStatus(JobStatus.MINING);
        job.setStartedAt(new Date());
        job.setLastUpdated(new Date());
        job.setWorkerId(workerIdProvider.getWorkerId());

        jobRepository.save(job);
    }

    public void updateJobStatusToCompleted(Job job) {
        job.setJobStatus(JobStatus.COMPLETED);
        job.setFinishedAt(new Date());
        job.setLastUpdated(new Date());

        jobRepository.save(job);
    }

    public void updateJobProgress(Job job, String cursor) {
        job.setLastUpdated(new Date());
        job.setCursor(cursor);

        jobRepository.save(job);
    }
}
