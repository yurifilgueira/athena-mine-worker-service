package com.projectathena.mineworkerservice.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Table(name = "mining_commits")
public class MiningCommit implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Transient
    private MiningResult miningResult;

    @Column("mining_result_id")
    private UUID miningResultId;

    @Column("author_id")
    private UUID authorId;

    @Column("committer_id")
    private UUID committerId;

    @Column
    private String oid;
    
    @Column
    private String message;
    
    @Column
    private String messageBody;
    
    @Column
    private Integer additions;
    
    @Column
    private Integer deletions;
    
    @Transient
    private GitAuthor author;
    
    @Transient
    private GitAuthor committer;
    
    @Column
    private Boolean authoredByCommitter;
    
    @Column
    private String commitUrl;
    
    @Column
    private Instant committedDate;

    public MiningCommit() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MiningResult getMiningResult() {
        return miningResult;
    }

    public void setMiningResult(MiningResult miningResult) {
        this.miningResult = miningResult;
        if (miningResult != null) {
            this.miningResultId = miningResult.getId();
        }
    }

    public GitAuthor getAuthor() {
        return author;
    }

    public void setAuthor(GitAuthor author) {
        this.author = author;
        if (author != null) {
            this.authorId = author.getId();
        }
    }

    public GitAuthor getCommitter() {
        return committer;

    }

    public void setCommitter(GitAuthor committer) {
        this.committer = committer;
        if (committer != null) {
            this.committerId = committer.getId();
        }
    }

    public UUID getMiningResultId() {
        return miningResultId;
    }

    public void setMiningResultId(UUID miningResultId) {
        this.miningResultId = miningResultId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getCommitterId() {
        return committerId;
    }

    public void setCommitterId(UUID committerId) {
        this.committerId = committerId;
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

    public Instant getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Instant committedDate) {
        this.committedDate = committedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MiningCommit that = (MiningCommit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
