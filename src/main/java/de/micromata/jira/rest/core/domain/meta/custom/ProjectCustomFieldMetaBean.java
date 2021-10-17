package de.micromata.jira.rest.core.domain.meta.custom;

import java.util.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.meta.fields.*;

/**
 * Created by cschulc on 16.03.16.
 */
public class ProjectCustomFieldMetaBean extends FieldMetaBean {

    private List<ProjectBean> allowedValues = new ArrayList<>();

    public ProjectCustomFieldMetaBean(FieldMetaBean fieldMetaBean) {
        super(fieldMetaBean);
    }

    public List<ProjectBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<ProjectBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
