package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;


public class StatusCategoryBean extends BaseBean {
    @Expose
    private String colorName;
    @Expose
    private String key;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
