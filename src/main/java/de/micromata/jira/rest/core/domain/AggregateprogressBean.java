package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.Expose;

public class AggregateprogressBean {
    @Expose
    private long percent;
    @Expose
    private long progress;
    @Expose
    private long total;

    public long getPercent() {
        return percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
