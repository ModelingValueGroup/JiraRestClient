package de.micromata.jira.rest.core.custom;

import java.lang.reflect.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.*;
import de.micromata.jira.rest.*;
import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.customFields.*;
import de.micromata.jira.rest.core.domain.field.*;
import de.micromata.jira.rest.core.domain.meta.custom.*;

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
        FieldBean fieldBean = JiraRestClient.getCustomFieldsMap().get(customFieldId);
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
