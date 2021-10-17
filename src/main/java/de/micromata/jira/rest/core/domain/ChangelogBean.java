package de.micromata.jira.rest.core.domain;

import java.util.*;

import com.google.gson.annotations.*;


public class ChangelogBean {
    @Expose
    private long          startAt;
    @Expose
    private long          maxResults;
    @Expose
    private long          total;
    @Expose
    private List<History> histories = new ArrayList<>();

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
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
