package de.micromata.jira.rest.core.domain.meta.fields;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import de.micromata.jira.rest.core.domain.VersionBean;

/**
 * Created by cschulc on 16.03.16.
 */
public class VersionsFieldMetaBean extends FieldMetaBean {

    @Expose
    private List<VersionBean> allowedValues = new ArrayList<>();

    public List<VersionBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<VersionBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
