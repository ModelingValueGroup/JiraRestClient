package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;

public class VotesBean extends BaseBean {
    @Expose
    private boolean hasVoted;
    @Expose
    private long    votes;

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}
