package de.micromata.jira.rest.core.domain;

import java.time.LocalDateTime;

import com.google.gson.annotations.Expose;

public class WorkEntryBean extends BaseBean {
    @Expose
    private AuthorBean author;
    @Expose
    private AuthorBean updateAuthor;
    @Expose
    private String     created;
    @Expose
    private String     updated;
    @Expose
    private String     started;
    @Expose
    private String     timeSpent;
    @Expose
    private long       timeSpentSeconds;
    @Expose
    private String     issueId;
    @Expose
    private String     comment;

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public AuthorBean getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(AuthorBean updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(long timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getStartedDate() {
        return toDate(started);
    }
}
