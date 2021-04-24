package de.micromata.jira.rest.core.domain.filter;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionsBean {
    @Expose
    private long         size;
    @Expose
    private List<Object> items = new ArrayList<>();
    @SerializedName("max-results")
    @Expose
    private long         maxResults;
    @SerializedName("start-index")
    @Expose
    private long         startIndex;
    @SerializedName("end-index")
    @Expose
    private long         endIndex;

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(long maxResults) {
        this.maxResults = maxResults;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }
}
