package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.configs.WorkerIdProvider;
import com.projectathena.mineworkerservice.model.dto.responses.JobSubmissionResponse;
import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final WorkerIdProvider workerIdProvider;
    private final Logger logger = LoggerFactory.getLogger(JobService.class);
    @Value(value = "${spring.application.name}")
    private String applicationName;
    private final static String BASE_URL_VERIFY_JOB_STATUS = "http://localhost:8080";

    public JobService(JobRepository jobRepository, WorkerIdProvider workerIdProvider, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.workerIdProvider = workerIdProvider;
        this.userRepository = userRepository;
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

    public JobSubmissionResponse publishJob(PublishJobRequest request){

        User user = userRepository.findByEmail(request.userEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName(request.userName());
                    newUser.setEmail(request.userEmail());
                    return userRepository.save(newUser);
                });

        if (user == null) {
            while (true) {
                logger.error("User is null");
            }
        }

        Job job = new Job();
        job.setRequestedBy(user);
        job.setJobStatus(JobStatus.PENDING);
        job.setCreatedAt(new Date());
        job.setGitRepositoryOwner(request.gitRepositoryOwner());
        job.setGitRepositoryName(request.gitRepositoryName());

        var jobEntity =jobRepository.save(job);

        String urlJobStatus = BASE_URL_VERIFY_JOB_STATUS + "/" + applicationName + "/jobs/status/" + jobEntity.getId();
        return new JobSubmissionResponse(jobEntity.getId(), jobEntity.getJobStatus(), urlJobStatus);
    }

    public List<Job> findJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByJobStatus(jobStatus);
    }

    public void updateJobToPending(Job job) {
        job.setJobStatus(JobStatus.PENDING);
        job.setLastUpdated(new Date());
        job.setWorkerId(null);

        jobRepository.save(job);

        logger.info("Job updated to PENDING: {}", job.getId());
    }

    public Optional<Job> findById(String id) {
        return jobRepository.findById(id);
    }

    public JobStatusResponse findJobStatusById(String id) {
        Optional<Job> job = jobRepository.findById(id);

        return job.map(value -> new JobStatusResponse(value.getId(), value.getJobStatus())).orElse(null);

    }
}
