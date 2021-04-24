package de.micromata.jira.rest.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class CommentsBean {
    @Expose
    private List<CommentBean> comments = new ArrayList<>();
    @Expose
    private long              maxResults;
    @Expose
    private long              startAt;
    @Expose
    private long              total;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

    public long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(long maxResults) {
        this.maxResults = maxResults;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
