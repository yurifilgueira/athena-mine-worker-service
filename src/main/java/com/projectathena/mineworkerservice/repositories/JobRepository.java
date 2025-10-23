package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.entities.User;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, UUID> {
    Mono<Job> findFirstByJobStatusOrderByCreatedAtAsc(JobStatus jobStatus);
    Flux<Job> findByJobStatus(JobStatus jobStatus);
    Mono<Job> findByRequestedByAndGitRepositoryNameAndGitRepositoryOwner(User requestedBy, String gitRepositoryName, String gitRepositoryOwner);
}
