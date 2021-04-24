package de.micromata.jira.rest.core.domain.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 29.10.2014
 */
public class IssueUpdate {

    @Expose
    private Map<String, Object>               fields = new HashMap<>();
    @Expose
    private Map<String, List<FieldOperation>> update = new HashMap<>();

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Map<String, List<FieldOperation>> getUpdate() {
        return update;
    }

    public void setUpdate(Map<String, List<FieldOperation>> update) {
        this.update = update;
    }
}
