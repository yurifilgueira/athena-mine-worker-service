package com.projectathena.mineworkerservice.model.entities;

import com.projectathena.mineworkerservice.model.enums.MiningStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "mining_results")
public class MiningResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    
    @Column
    private UUID jobId;
    
    @Column
    private String repositoryOwner;
    
    @Column
    private String repositoryName;
    
    @Column
    private Instant startedAt;
    
    @Column
    private Instant lastUpdatedAt;
    
    @Column
    private Integer totalCommits;
    
    @Column
    private MiningStatus status;
    
    @Column
    private String lastCursor;
    
    @Transient
    private List<MiningCommit> commits;

    public MiningResult() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public void setRepositoryOwner(String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Integer getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(Integer totalCommits) {
        this.totalCommits = totalCommits;
    }

    public MiningStatus getStatus() {
        return status;
    }

    public void setStatus(MiningStatus status) {
        this.status = status;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public void addCommits(MiningCommit miningCommit) {
        commits.add(miningCommit);
    }

    public List<MiningCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<MiningCommit> commits) {
        this.commits = commits;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MiningResult that = (MiningResult) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
