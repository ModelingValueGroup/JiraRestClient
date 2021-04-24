package de.micromata.jira.rest.core.custom;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.customFields.CustomFieldType;
import de.micromata.jira.rest.core.domain.meta.FieldsMetaBean;
import de.micromata.jira.rest.core.domain.meta.IssueTypeMetaBean;
import de.micromata.jira.rest.core.domain.meta.MetaBean;
import de.micromata.jira.rest.core.domain.meta.ProjectMetaBean;
import de.micromata.jira.rest.core.domain.meta.custom.ProjectCustomFieldMetaBean;
import de.micromata.jira.rest.core.domain.meta.custom.ValueMetaBean;
import de.micromata.jira.rest.core.domain.meta.custom.ValuesCustomFieldMetaBean;
import de.micromata.jira.rest.core.domain.meta.custom.VersionCustomFieldMetaBean;
import de.micromata.jira.rest.core.domain.meta.fields.FieldMetaBean;

/**
 * Created by cschulc on 16.03.16.
 */
public class MetaBeanDeserializer extends BaseDeserializer implements JsonDeserializer<MetaBean> {
    public static final String ALLOWED_VALUES     = "allowedValues";

    private final Map<String, FieldMetaBean> customFieldsMetaBeanCache = new HashMap<>();

    @Override
    public MetaBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MetaBean metaBean = gson.fromJson(json, MetaBean.class);

        Map<String, ProjectMetaBean> projectsMetaMap = new HashMap<>();
        List<ProjectMetaBean>        projects        = metaBean.getProjects();
        for (ProjectMetaBean project : projects) {
            projectsMetaMap.put(project.getKey(), project);
        }
        JsonObject  jsonObject      = json.getAsJsonObject();
        JsonElement projectsElement = jsonObject.get("projects");
        JsonArray   projectsArray   = projectsElement.getAsJsonArray();
        for (JsonElement jsonElement : projectsArray) {
            JsonObject  projectObject = jsonElement.getAsJsonObject();
            JsonElement keyElement    = projectObject.get("key");
            String      key           = keyElement.getAsString();
            if (projectsMetaMap.containsKey(key)) {
                ProjectMetaBean projectMetaBean = projectsMetaMap.get(key);
                processProjectMeta(projectMetaBean, projectObject);
            }
        }
        return metaBean;
    }

    private void processProjectMeta(ProjectMetaBean projectMetaBean, JsonObject projectObject) {
        List<IssueTypeMetaBean>        issuetypes           = projectMetaBean.getIssuetypes();
        Map<String, IssueTypeMetaBean> issueTypeMetaBeanMap = new HashMap<>();
        for (IssueTypeMetaBean issuetype : issuetypes) {
            String name = issuetype.getName();
            issueTypeMetaBeanMap.put(name, issuetype);
        }

        JsonElement issuetypesElement = projectObject.get("issuetypes");
        JsonArray   issuetypesArray   = issuetypesElement.getAsJsonArray();
        for (JsonElement jsonElement : issuetypesArray) {
            JsonObject  issuetypeObject = jsonElement.getAsJsonObject();
            JsonElement nameElement     = issuetypeObject.get("name");
            String      name            = nameElement.getAsString();
            if (issueTypeMetaBeanMap.containsKey(name)) {
                IssueTypeMetaBean issueTypeMetaBean = issueTypeMetaBeanMap.get(name);
                processIssueType(issueTypeMetaBean, issuetypeObject);
            }
        }
    }


    private void processIssueType(IssueTypeMetaBean issueTypeMetaBean, JsonObject issuetypeObject) {
        FieldsMetaBean                      fields        = issueTypeMetaBean.getFields();
        JsonElement                         fieldsElement = issuetypeObject.get("fields");
        JsonObject                          fieldsObject  = fieldsElement.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries       = fieldsObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String customFieldId = entry.getKey();
            if (customFieldId.startsWith("customfield_")) {
                if (customFieldsMetaBeanCache.containsKey(customFieldId)) {
                    FieldMetaBean fieldMetaBean = customFieldsMetaBeanCache.get(customFieldId);
                    fields.getCustom().add(fieldMetaBean);
                } else {
                    FieldMetaBean fieldMetaBean = extractCustomFieldMeta(customFieldId, entry.getValue());
                    customFieldsMetaBeanCache.put(customFieldId, fieldMetaBean);
                    fields.getCustom().add(fieldMetaBean);
                }
            }
        }
    }

    private FieldMetaBean extractCustomFieldMeta(String key, JsonElement json) {
        FieldMetaBean   fieldMetaBean   = gson.fromJson(json, FieldMetaBean.class);
        CustomFieldType customFieldType = getCustomFieldType(key);
        if (customFieldType == null) {
            return fieldMetaBean;
        }
        switch (customFieldType) {
        case URL:
            return fieldMetaBean;
        case DATE:
            return fieldMetaBean;
        case DATETIME:
            return fieldMetaBean;
        case TEXT:
            return fieldMetaBean;
        case TEXTAREA:
            return fieldMetaBean;
        case FLOAT:
            return fieldMetaBean;
        case SELECT:
            return getValueCustomFieldMetaBean(json, fieldMetaBean);
        case RADIO:
            return getValueCustomFieldMetaBean(json, fieldMetaBean);
        case USER:
            return fieldMetaBean;
        case PROJECT:
            ProjectCustomFieldMetaBean projectCustomFieldMetaBean = new ProjectCustomFieldMetaBean(fieldMetaBean);
            JsonObject projectCustomFieldObject = json.getAsJsonObject();
            JsonElement projectCustomAllowedValues = projectCustomFieldObject.get(ALLOWED_VALUES);
            JsonArray projectCustomAllowedValuesArray = projectCustomAllowedValues.getAsJsonArray();
            List<ProjectBean> projects = gson.fromJson(projectCustomAllowedValuesArray, LIST_OF_PROJECT);
            projectCustomFieldMetaBean.setAllowedValues(projects);
            return projectCustomFieldMetaBean;
        case CASCADING:
            return getValueCustomFieldMetaBean(json, fieldMetaBean);
        case GROUP:
            return fieldMetaBean;
        case VERSION:
            return getVersionCustomFieldMeta(json, fieldMetaBean);
        case LABELS:
            return fieldMetaBean;
        case MULTISELECT:
            return getValueCustomFieldMetaBean(json, fieldMetaBean);
        case CHECKBOX:
            return getValueCustomFieldMetaBean(json, fieldMetaBean);
        case MULTIUSER:
            return fieldMetaBean;
        case MULTIVERSION:
            return getVersionCustomFieldMeta(json, fieldMetaBean);
        case MULTIGROUP:
            return fieldMetaBean;
        default:
            return fieldMetaBean;
        }
    }

    private FieldMetaBean getValueCustomFieldMetaBean(JsonElement json, FieldMetaBean fieldMetaBean) {
        ValuesCustomFieldMetaBean valuesCustomFieldMetaBean           = new ValuesCustomFieldMetaBean(fieldMetaBean);
        JsonObject                valuesFieldObject                   = json.getAsJsonObject();
        JsonElement               valuesCustomFieldAllowedValues      = valuesFieldObject.get(ALLOWED_VALUES);
        JsonArray                 valuesCustomFieldAllowedValuesArray = valuesCustomFieldAllowedValues.getAsJsonArray();
        List<ValueMetaBean>       mutliSelectValues                   = gson.fromJson(valuesCustomFieldAllowedValuesArray, LIST_OF_VALUE_META);
        valuesCustomFieldMetaBean.setAllowedValues(mutliSelectValues);
        return valuesCustomFieldMetaBean;
    }

    private FieldMetaBean getVersionCustomFieldMeta(JsonElement json, FieldMetaBean fieldMetaBean) {
        VersionCustomFieldMetaBean versionCustomFieldMetaBean           = new VersionCustomFieldMetaBean(fieldMetaBean);
        JsonObject                 versionCustomFieldObject             = json.getAsJsonObject();
        JsonElement                versionCustomFieldAllowedValues      = versionCustomFieldObject.get(ALLOWED_VALUES);
        JsonArray                  versionCustomFieldAllowedValuesArray = versionCustomFieldAllowedValues.getAsJsonArray();
        List<VersionBean>          versions                             = gson.fromJson(versionCustomFieldAllowedValuesArray, LIST_OF_VERSION);
        versionCustomFieldMetaBean.setAllowedValues(versions);
        return versionCustomFieldMetaBean;
    }
}
