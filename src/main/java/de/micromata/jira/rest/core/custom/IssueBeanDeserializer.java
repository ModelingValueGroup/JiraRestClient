package de.micromata.jira.rest.core.custom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.micromata.jira.rest.core.domain.FieldsBean;
import de.micromata.jira.rest.core.domain.IssueBean;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.UserBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.customFields.CascadingValueBean;
import de.micromata.jira.rest.core.domain.customFields.CustomFieldBaseBean;
import de.micromata.jira.rest.core.domain.customFields.CustomFieldType;
import de.micromata.jira.rest.core.domain.customFields.GroupSelectBean;
import de.micromata.jira.rest.core.domain.customFields.MultiValueBean;
import de.micromata.jira.rest.core.domain.customFields.ProjectSelectBean;
import de.micromata.jira.rest.core.domain.customFields.SingleValueBean;
import de.micromata.jira.rest.core.domain.customFields.UserSelectBean;
import de.micromata.jira.rest.core.domain.customFields.ValueBean;
import de.micromata.jira.rest.core.domain.customFields.VersionSelectBean;

/**
 * Created by cschulc on 18.02.16.
 */
public class IssueBeanDeserializer extends BaseDeserializer implements JsonDeserializer<IssueBean> {
    @Override
    public IssueBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        IssueBean  issueBean = gson.fromJson(json, IssueBean.class);
        FieldsBean fields    = issueBean.getFields();
        if (fields == null) {
            return issueBean;
        }
        List<CustomFieldBaseBean> customFieldBean = extractCustomFieldBeans(json);
        fields.setCustomFields(customFieldBean);
        return issueBean;
    }

    private List<CustomFieldBaseBean> extractCustomFieldBeans(JsonElement json) {
        List<CustomFieldBaseBean> retval   = new ArrayList<>();
        JsonObject                issueObj = json.getAsJsonObject();
        JsonElement               fields   = issueObj.get("fields");
        if (fields == null) {
            return retval;
        }
        JsonObject                          fieldsObj = fields.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries   = fieldsObj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            if (key.startsWith("customfield_")) {
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive()) {
                    CustomFieldBaseBean customField = getPrimitiveCustomField(key, value);
                    if (customField != null) {
                        customField.setId(key);
                        retval.add(customField);
                    }
                } else if (value.isJsonObject()) {
                    CustomFieldBaseBean customField = getObjectCustomField(key, value);
                    if (customField != null) {
                        customField.setId(key);
                        retval.add(customField);
                    }
                } else if (value.isJsonArray()) {
                    CustomFieldBaseBean arrayCustomField = getArrayCustomField(key, value);
                    if (arrayCustomField != null) {
                        arrayCustomField.setId(key);
                        retval.add(arrayCustomField);
                    }
                }
            }
        }
        return retval;
    }

    private CustomFieldBaseBean getPrimitiveCustomField(String key, JsonElement jsonElement) {
        SingleValueBean retval    = new SingleValueBean();
        String          value     = jsonElement.getAsString();
        ValueBean       valueBean = new ValueBean();
        valueBean.setValue(value);
        retval.setValue(valueBean);
        CustomFieldType customFieldType = getCustomFieldType(key);
        if (customFieldType == null) {
            return null;
        }
        retval.setType(customFieldType);
        return retval;
    }

    private CustomFieldBaseBean getObjectCustomField(String id, JsonElement jsonElement) {
        CustomFieldType customFieldType = getCustomFieldType(id);
        if (customFieldType == null) {
            return null;
        }
        switch (customFieldType) {
        case SELECT:
            ValueBean valueBean = gson.fromJson(jsonElement, ValueBean.class);
            SingleValueBean singleValueBean = new SingleValueBean();
            singleValueBean.setType(CustomFieldType.SELECT);
            singleValueBean.setValue(valueBean);
            return singleValueBean;
        case RADIO:
            ValueBean optionValue = gson.fromJson(jsonElement, ValueBean.class);
            SingleValueBean option = new SingleValueBean();
            option.setType(CustomFieldType.RADIO);
            option.setValue(optionValue);
            return option;
        case USER:
            UserBean userBean = gson.fromJson(jsonElement, UserBean.class);
            UserSelectBean userSelectBean = new UserSelectBean();
            userSelectBean.setType(CustomFieldType.USER);
            userSelectBean.getUsers().add(userBean);
            return userSelectBean;
        case PROJECT:
            ProjectBean projectBean = gson.fromJson(jsonElement, ProjectBean.class);
            ProjectSelectBean projectSelectBean = new ProjectSelectBean();
            projectSelectBean.setType(CustomFieldType.PROJECT);
            projectSelectBean.setProject(projectBean);
            return projectSelectBean;
        case CASCADING:
            ValueBean cascadingValue = gson.fromJson(jsonElement, ValueBean.class);
            CascadingValueBean cascadingValueBean = new CascadingValueBean();
            cascadingValueBean.setType(CustomFieldType.CASCADING);
            cascadingValueBean.setValue(cascadingValue);
            return cascadingValueBean;
        case GROUP:
            ValueBean groupValue = gson.fromJson(jsonElement, ValueBean.class);
            GroupSelectBean groupSelectBean = new GroupSelectBean();
            groupSelectBean.setType(CustomFieldType.GROUP);
            groupSelectBean.getGroups().add(groupValue);
            return groupSelectBean;
        case VERSION:
            VersionBean versionBean = gson.fromJson(jsonElement, VersionBean.class);
            VersionSelectBean versionSelectBean = new VersionSelectBean();
            versionSelectBean.setType(CustomFieldType.VERSION);
            versionSelectBean.getVersions().add(versionBean);
            return versionSelectBean;
        default:
            return null;
        }
    }

    private CustomFieldBaseBean getArrayCustomField(String id, JsonElement json) {
        CustomFieldType customFieldType = getCustomFieldType(id);
        if (customFieldType == null) {
            return null;
        }
        switch (customFieldType) {
        case LABELS:
            List<String> labels = gson.fromJson(json, COLLECTION_OF_STRING);
            MultiValueBean labelsBean = new MultiValueBean();
            labelsBean.setType(CustomFieldType.LABELS);
            for (String label : labels) {
                ValueBean valueBean = new ValueBean();
                valueBean.setValue(label);
                labelsBean.getValues().add(valueBean);
            }
            return labelsBean;
        case MULTISELECT:
            List<ValueBean> multiSelectValues = gson.fromJson(json, COLLECTION_OF_VALUE);
            MultiValueBean multiSelectBean = new MultiValueBean();
            multiSelectBean.setType(CustomFieldType.MULTISELECT);
            multiSelectBean.setValues(multiSelectValues);
            return multiSelectBean;
        case CHECKBOX:
            List<ValueBean> checkboxValues = gson.fromJson(json, COLLECTION_OF_VALUE);
            MultiValueBean checkboxBean = new MultiValueBean();
            checkboxBean.setType(CustomFieldType.CHECKBOX);
            checkboxBean.setValues(checkboxValues);
            return checkboxBean;
        case MULTIUSER:
            List<UserBean> userSelectValues = gson.fromJson(json, COLLECTION_OF_USER);
            UserSelectBean userSelectBean = new UserSelectBean();
            userSelectBean.setType(CustomFieldType.MULTIUSER);
            userSelectBean.setUsers(userSelectValues);
            return userSelectBean;
        case MULTIVERSION:
            List<VersionBean> versionSelectValues = gson.fromJson(json, COLLECTION_OF_VERSION);
            VersionSelectBean versionSelectBean = new VersionSelectBean();
            versionSelectBean.setType(CustomFieldType.MULTIVERSION);
            versionSelectBean.setVersions(versionSelectValues);
            return versionSelectBean;
        case MULTIGROUP:
            List<ValueBean> groupSelectValues = gson.fromJson(json, COLLECTION_OF_VALUE);
            GroupSelectBean groupSelectBean = new GroupSelectBean();
            groupSelectBean.setType(CustomFieldType.MULTIGROUP);
            groupSelectBean.setGroups(groupSelectValues);
            return groupSelectBean;
        default:
            return null;
        }
    }
}
