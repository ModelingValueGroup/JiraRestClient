package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;


public class WatchesBean extends BaseBean {
    @Expose
    private boolean isWatching;
    @Expose
    private long    watchCount;

    public boolean isIsWatching() {
        return isWatching;
    }

    public void setIsWatching(boolean isWatching) {
        this.isWatching = isWatching;
    }

    public long getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(long watchCount) {
        this.watchCount = watchCount;
    }
}
