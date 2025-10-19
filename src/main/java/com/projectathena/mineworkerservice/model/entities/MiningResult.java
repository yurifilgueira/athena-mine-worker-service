package com.projectathena.mineworkerservice.model.entities;

import com.projectathena.mineworkerservice.model.enums.MiningStatus;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(name = "mining_results")
@Entity
public class MiningResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String jobId;
    
    @Column(nullable = false)
    private String repositoryOwner;
    
    @Column(nullable = false)
    private String repositoryName;
    
    @Column(nullable = false)
    private Date startedAt;
    
    @Column
    private Date lastUpdatedAt;
    
    @Column
    private Integer totalCommits;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MiningStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String lastCursor;
    
    @OneToMany(mappedBy = "miningResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MiningCommit> commits;

    public MiningResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
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

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
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
