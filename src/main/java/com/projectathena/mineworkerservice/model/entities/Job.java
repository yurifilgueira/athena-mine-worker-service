package com.projectathena.mineworkerservice.model.entities;

import com.projectathena.mineworkerservice.model.enums.JobStatus;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "jobs")
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id")
    private User requestedBy;
    @Enumerated(EnumType.STRING)
    @Column(length = 80, nullable = false)
    private JobStatus jobStatus;
    @Column(length = 100, nullable = false)
    private LocalDateTime createdAt;
    @Column(length = 100)
    private LocalDateTime startedAt;
    @Column(length = 100)
    private LocalDateTime finishedAt;
    @Column(length = 100)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUpdated;
    @Column(length = 100, nullable = false)
    private String gitRepositoryOwner;
    @Column(length = 100, nullable = false)
    private String gitRepositoryName;
    @Column(length = 100)
    private String cursor;
    @Column(length = 100)
    private String workerId;

    public Job() {
    }

    public Job(String id, User requestedBy, JobStatus jobStatus, LocalDateTime createdAt, LocalDateTime startedAt, LocalDateTime finishedAt, LocalDateTime lastUpdated, String gitRepositoryOwner, String gitRepositoryName, String cursor, String workerId) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
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
