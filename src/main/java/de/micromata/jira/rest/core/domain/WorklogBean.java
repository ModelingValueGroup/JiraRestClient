package de.micromata.jira.rest.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class WorklogBean {
    @Expose
    private long                maxResults;
    @Expose
    private long                startAt;
    @Expose
    private long                total;
    @Expose
    private List<WorkEntryBean> worklogs = new ArrayList<>();

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

    public List<WorkEntryBean> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(List<WorkEntryBean> worklogs) {
        this.worklogs = worklogs;
    }
}
