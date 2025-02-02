package de.micromata.jira.rest.core.domain.meta.custom;

import java.util.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.meta.fields.*;

/**
 * Created by cschulc on 16.03.16.
 */
public class VersionCustomFieldMetaBean extends FieldMetaBean {

    private List<VersionBean> allowedValues = new ArrayList<>();

    public VersionCustomFieldMetaBean(FieldMetaBean fieldMetaBean) {
        super(fieldMetaBean);
    }

    public List<VersionBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<VersionBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
