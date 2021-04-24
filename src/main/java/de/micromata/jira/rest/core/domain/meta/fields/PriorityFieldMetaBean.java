package de.micromata.jira.rest.core.domain.meta.fields;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import de.micromata.jira.rest.core.domain.PriorityBean;

/**
 * Created by cschulc on 16.03.16.
 */
public class PriorityFieldMetaBean extends FieldMetaBean {

    @Expose
    private List<PriorityBean> allowedValues = new ArrayList<>();

    public List<PriorityBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<PriorityBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
