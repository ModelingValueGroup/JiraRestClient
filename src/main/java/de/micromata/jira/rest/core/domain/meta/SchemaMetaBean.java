package de.micromata.jira.rest.core.domain.meta;

import com.google.gson.annotations.*;

/**
 * Created by cschulc on 16.03.16.
 */
public class SchemaMetaBean {
    @Expose
    private String type;
    @Expose
    private String items;
    @Expose
    private String system;
    @Expose
    private String custom;
    @Expose
    private long   customId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
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
