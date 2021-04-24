package de.micromata.jira.rest.core;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.google.gson.stream.JsonReader;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.client.SystemClient;
import de.micromata.jira.rest.core.domain.AttachmentMetaBean;
import de.micromata.jira.rest.core.domain.IssuetypeBean;
import de.micromata.jira.rest.core.domain.PriorityBean;
import de.micromata.jira.rest.core.domain.StatusBean;
import de.micromata.jira.rest.core.domain.field.CreateFieldBean;
import de.micromata.jira.rest.core.domain.field.FieldBean;
import de.micromata.jira.rest.core.domain.system.ConfigurationBean;
import de.micromata.jira.rest.core.misc.RestParamConstants;
import de.micromata.jira.rest.core.misc.RestPathConstants;
import de.micromata.jira.rest.core.util.RestException;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 31.07.2014
 */
public class SystemClientImpl extends BaseClient implements SystemClient, RestParamConstants, RestPathConstants {

    public SystemClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    @Override
    public Future<ConfigurationBean> getConfiguration() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(CONFIGURATION)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, ConfigurationBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<IssuetypeBean>> getIssueTypes() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(ISSUETPYES)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_ISSUETYPES);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });

    }

    public Future<List<StatusBean>> getStates() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(STATUS)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_STATUS);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<PriorityBean>> getPriorities() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(PRIORITY)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_PRIORITY);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<FieldBean>> getAllFields() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(FIELD)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_FIELD);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<FieldBean>> getAllCustomFields() {
        return submit(() -> {
            List<FieldBean>         retval    = new ArrayList<>();
            Future<List<FieldBean>> allFields = getAllFields();
            List<FieldBean>         fieldBeen = allFields.get();
            for (FieldBean fieldBean : fieldBeen) {
                if (fieldBean.isCustom()) {
                    retval.add(fieldBean);
                }
            }
            return retval;
        });
    }

    public Future<FieldBean> getCustomFieldById(final String id) {
        return submit(() -> {
            Future<List<FieldBean>> allFields = getAllFields();
            List<FieldBean>         fieldBeen = allFields.get();
            for (FieldBean fieldBean : fieldBeen) {
                if (!fieldBean.isCustom()) {
                    continue;
                }
                if (fieldBean.getId().contains(id)) {
                    return fieldBean;
                }
            }
            return null;
        });
    }

    public Future<AttachmentMetaBean> getAttachmentMeta() {
        return null;
    }

    @Override
    public Future<FieldBean> createCustomField(CreateFieldBean customField) {
        return submit(() -> {
            try (CloseableHttpResponse response = executePost(customField, FIELD)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_CREATED) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, FieldBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }
}
