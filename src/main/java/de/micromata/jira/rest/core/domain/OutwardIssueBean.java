package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;

public class OutwardIssueBean extends BaseBean {
    @Expose
    private String                 key;
    @Expose
    private OutwardIssueFieldsBean fields;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OutwardIssueFieldsBean getFields() {
        return fields;
    }

    public void setFields(OutwardIssueFieldsBean fields) {
        this.fields = fields;
    }
}
