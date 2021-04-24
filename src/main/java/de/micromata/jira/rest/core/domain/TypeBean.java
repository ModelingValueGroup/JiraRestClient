package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.Expose;

public class TypeBean extends BaseBean {
    @Expose
    private String inward;
    @Expose
    private String outward;

    public String getInward() {
        return inward;
    }

    public void setInward(String inward) {
        this.inward = inward;
    }

    public String getOutward() {
        return outward;
    }

    public void setOutward(String outward) {
        this.outward = outward;
    }
}
