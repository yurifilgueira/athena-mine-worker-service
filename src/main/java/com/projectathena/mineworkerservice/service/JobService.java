package com.projectathena.mineworkerservice.service;

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

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Optional<Job> findById(String id) {
        return jobRepository.findById(id);
    }

    public Optional<Job> findJobByStatus(JobStatus status) {
        return jobRepository.findByJobStatus(status);
    }

    @Transactional
    public Optional<Job> findPendingJob() {
        return jobRepository.findFirstByJobStatusOrderByCreatedAtAsc(JobStatus.PENDING);
    }

    public void updateJobStatusToMining(Job job) {
        job.setStatus(JobStatus.MINING);
        job.setStartedAt(new Date());

        jobRepository.save(job);
    }

    public void updateJobStatusToCompleted(Job job) {
        job.setStatus(JobStatus.COMPLETED);
        job.setFinishedAt(new Date());

        jobRepository.save(job);
    }
}
