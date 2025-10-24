package com.projectathena.mineworkerservice.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Table(name = "mining_commits")
@Entity
public class MiningCommit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mining_result_id", nullable = false)
    @JsonBackReference
    private MiningResult miningResult;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String oid;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(columnDefinition = "TEXT")
    private String messageBody;
    
    @Column
    private Integer additions;
    
    @Column
    private Integer deletions;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private GitAuthor author;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "committer_id")
    private GitAuthor committer;
    
    @Column
    private Boolean authoredByCommitter;
    
    @Column(columnDefinition = "TEXT")
    private String commitUrl;
    
    @Column(nullable = false)
    private LocalDateTime committedDate;

    public MiningCommit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MiningResult getMiningResult() {
        return miningResult;
    }

    public void setMiningResult(MiningResult miningResult) {
        this.miningResult = miningResult;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Integer getAdditions() {
        return additions;
    }

    public void setAdditions(Integer additions) {
        this.additions = additions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public GitAuthor getAuthor() {
        return author;
    }

    public void setAuthor(GitAuthor author) {
        this.author = author;
    }

    public GitAuthor getCommitter() {
        return committer;
    }

    public void setCommitter(GitAuthor committer) {
        this.committer = committer;
    }

    public Boolean getAuthoredByCommitter() {
        return authoredByCommitter;
    }

    public void setAuthoredByCommitter(Boolean authoredByCommitter) {
        this.authoredByCommitter = authoredByCommitter;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }

    public LocalDateTime getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(LocalDateTime committedDate) {
        this.committedDate = committedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MiningCommit that = (MiningCommit) o;
        return Objects.equals(oid, that.oid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(oid);
    }
}
