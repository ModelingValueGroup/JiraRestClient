package de.micromata.jira.rest.core.domain.field;

import java.util.*;

import com.google.gson.annotations.*;
import de.micromata.jira.rest.core.domain.*;

public class FieldBean extends BaseBean {
    @Expose
    private boolean      custom;
    @Expose
    private boolean      orderable;
    @Expose
    private boolean      navigable;
    @Expose
    private boolean      searchable;
    @Expose
    private List<String> clauseNames = new ArrayList<>();
    @Expose
    private SchemaBean   schema;

    public List<String> getClauseNames() {
        return clauseNames;
    }

    public void setClauseNames(List<String> clauseNames) {
        this.clauseNames = clauseNames;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isNavigable() {
        return navigable;
    }

    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public SchemaBean getSchema() {
        return schema;
    }

    public void setSchema(SchemaBean schema) {
        this.schema = schema;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }
}
