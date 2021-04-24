package de.micromata.jira.rest.core.domain.meta.fields;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import de.micromata.jira.rest.core.domain.meta.SchemaMetaBean;

/**
 * Created by cschulc on 16.03.16.
 */
public class FieldMetaBean {

    @Expose
    private boolean        required;
    @Expose
    private SchemaMetaBean schema;
    @Expose
    private String         name;
    @Expose
    private boolean        hasDefaultValue;
    @Expose
    private List<String>   operations = new ArrayList<>();

    public FieldMetaBean() {
    }

    public FieldMetaBean(FieldMetaBean fieldMetaBean) {
        this.schema = fieldMetaBean.getSchema();
        this.setName(fieldMetaBean.getName());
        this.setHasDefaultValue(fieldMetaBean.isHasDefaultValue());
        this.setOperations(fieldMetaBean.getOperations());
        this.setRequired(fieldMetaBean.isRequired());
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public SchemaMetaBean getSchema() {
        return schema;
    }

    public void setSchema(SchemaMetaBean schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasDefaultValue() {
        return hasDefaultValue;
    }

    public void setHasDefaultValue(boolean hasDefaultValue) {
        this.hasDefaultValue = hasDefaultValue;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
