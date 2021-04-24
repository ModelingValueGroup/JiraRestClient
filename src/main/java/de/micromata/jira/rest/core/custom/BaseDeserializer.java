package de.micromata.jira.rest.core.custom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.UserBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.customFields.CustomFieldType;
import de.micromata.jira.rest.core.domain.customFields.ValueBean;
import de.micromata.jira.rest.core.domain.field.FieldBean;
import de.micromata.jira.rest.core.domain.meta.custom.ValueMetaBean;

/**
 * Created by cschulc on 16.03.16.
 */
public abstract class BaseDeserializer {
    public static final Type COLLECTION_OF_VALUE   = new TypeToken<Collection<ValueBean>>() {
    }.getType();
    public static final Type COLLECTION_OF_USER    = new TypeToken<Collection<UserBean>>() {
    }.getType();
    public static final Type COLLECTION_OF_VERSION = new TypeToken<Collection<VersionBean>>() {
    }.getType();
    public static final Type COLLECTION_OF_STRING  = new TypeToken<Collection<String>>() {
    }.getType();
    public static final Type LIST_OF_PROJECT       = new TypeToken<ArrayList<ProjectBean>>() {
    }.getType();
    public static final Type LIST_OF_VERSION       = new TypeToken<ArrayList<VersionBean>>() {
    }.getType();
    public static final Type LIST_OF_VALUE_META    = new TypeToken<ArrayList<ValueMetaBean>>() {
    }.getType();

    protected final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    protected CustomFieldType getCustomFieldType(String customFieldId) {
        FieldBean fieldBean = JiraRestClient.getCustomfields().get(customFieldId);
        if (fieldBean == null) {
            return null;
        }
        String custom = fieldBean.getSchema().getCustom();
        if (custom == null) {
            return null;
        }
        CustomFieldType[] values = CustomFieldType.values();
        for (CustomFieldType value : values) {
            if (custom.equals(value.getJiraName())) {
                return value;
            }
        }
        return null;
    }
}
