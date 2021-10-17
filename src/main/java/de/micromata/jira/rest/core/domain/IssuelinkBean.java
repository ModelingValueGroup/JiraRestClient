package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;

public class IssuelinkBean extends BaseBean {
    @Expose
    private TypeBean         type;
    @Expose
    private OutwardIssueBean outwardIssue;

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public OutwardIssueBean getOutwardIssue() {
        return outwardIssue;
    }

    public void setOutwardIssue(OutwardIssueBean outwardIssue) {
        this.outwardIssue = outwardIssue;
    }
}
