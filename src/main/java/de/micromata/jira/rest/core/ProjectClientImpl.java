package de.micromata.jira.rest.core;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;

import com.google.gson.stream.JsonReader;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.client.ProjectClient;
import de.micromata.jira.rest.core.domain.ComponentBean;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.meta.MetaBean;
import de.micromata.jira.rest.core.misc.RestParamConstants;
import de.micromata.jira.rest.core.misc.RestPathConstants;
import de.micromata.jira.rest.core.util.RestException;

/**
 * User: Christian
 * Date: 31.07.2014
 */
public class ProjectClientImpl extends BaseClient implements ProjectClient, RestParamConstants, RestPathConstants {
    public ProjectClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public Future<ProjectBean> getProjectByKey(final String projectKey) {
        Validate.notNull(projectKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(PROJECT, projectKey)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, ProjectBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<ProjectBean>> getAllProjects() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(PROJECT)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_PROJECT);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<VersionBean>> getProjectVersions(final String projectKey) {
        Validate.notNull(projectKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(PROJECT, projectKey, VERSIONS)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_VERSION);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<ComponentBean>> getProjectComponents(final String projectKey) {
        Validate.notNull(projectKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(PROJECT, projectKey, COMPONENTS)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_COMPONENT);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    @Override
    public Future<MetaBean> getIssueTypesMetaForProject(final String projectKey) {
        Validate.notNull(projectKey);
        return submit(() -> {
            URIBuilder uriBuilder = buildPath(ISSUE, CREATEMETA)
                    .addParameter(PROJECTKEYS, projectKey)
                    .addParameter(EXPAND, "projects.issuetypes.fields");
            try (CloseableHttpResponse response = executeGet(uriBuilder)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, MetaBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }
}
