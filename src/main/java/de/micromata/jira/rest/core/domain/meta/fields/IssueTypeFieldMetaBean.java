package de.micromata.jira.rest.core.domain.meta.fields;

import java.util.*;

import com.google.gson.annotations.*;
import de.micromata.jira.rest.core.domain.*;

/**
 * Created by cschulc on 16.03.16.
 */
public class IssueTypeFieldMetaBean extends FieldMetaBean {

    @Expose
    private List<IssuetypeBean> allowedValues = new ArrayList<>();

    public List<IssuetypeBean> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<IssuetypeBean> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
