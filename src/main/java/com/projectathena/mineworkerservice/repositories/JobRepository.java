package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.enums.JobStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Job> findFirstByJobStatusOrderByCreatedAtAsc(JobStatus jobStatus);
    List<Job> findByJobStatus(JobStatus jobStatus);
}
