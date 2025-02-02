package de.micromata.jira.rest.core.domain.field;

import com.google.gson.annotations.*;


public class SchemaBean {
    @Expose
    private String type;
    @Expose
    private String system;
    @Expose
    private String custom;
    @Expose
    private long   customId;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public long getCustomId() {
        return customId;
    }

    public void setCustomId(long customId) {
        this.customId = customId;
    }
}
