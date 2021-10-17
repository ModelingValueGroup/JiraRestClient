package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;

public class ProjectCategoryBean extends BaseBean {
    @Expose
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
