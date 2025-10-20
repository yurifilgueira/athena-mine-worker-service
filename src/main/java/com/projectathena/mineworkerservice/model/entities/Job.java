package com.projectathena.mineworkerservice.model.entities;

import com.projectathena.mineworkerservice.model.enums.JobStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Table(name = "jobs")
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    @Transient
    private User requestedBy;
    @Column("requested_by_id")
    private UUID requestedById;
    @Column
    private JobStatus jobStatus;
    @Column
    private Instant createdAt;
    @Column
    private Instant startedAt;
    @Column
    private Instant finishedAt;
    @Column
    private Instant lastUpdated;
    @Column
    private String gitRepositoryOwner;
    @Column
    private String gitRepositoryName;
    @Column
    private String cursor;
    @Column
    private String workerId;

    public Job() {
    }

    public Job(UUID id, User requestedBy, JobStatus jobStatus, Instant createdAt, Instant startedAt, Instant finishedAt, Instant lastUpdated, String gitRepositoryOwner, String gitRepositoryName, String cursor, String workerId) {
        this.id = id;
        this.requestedBy = requestedBy;
        this.jobStatus = jobStatus;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.lastUpdated = lastUpdated;
        this.gitRepositoryOwner = gitRepositoryOwner;
        this.gitRepositoryName = gitRepositoryName;
        this.cursor = cursor;
        this.workerId = workerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getGitRepositoryOwner() {
        return gitRepositoryOwner;
    }

    public void setGitRepositoryOwner(String gitRepositoryOwner) {
        this.gitRepositoryOwner = gitRepositoryOwner;
    }

    public String getGitRepositoryName() {
        return gitRepositoryName;
    }

    public void setGitRepositoryName(String gitRepositoryName) {
        this.gitRepositoryName = gitRepositoryName;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public UUID getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(UUID requestedById) {
        this.requestedById = requestedById;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
