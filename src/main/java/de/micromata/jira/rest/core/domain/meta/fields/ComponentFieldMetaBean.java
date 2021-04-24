package de.micromata.jira.rest.core.domain.meta.fields;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import de.micromata.jira.rest.core.domain.ComponentBean;

/**
 * Created by cschulc on 16.03.16.
 */
public class ComponentFieldMetaBean extends FieldMetaBean {

    @Expose
    private List<ComponentBean> allowedValues = new ArrayList<>();

    public List<ComponentBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<ComponentBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
