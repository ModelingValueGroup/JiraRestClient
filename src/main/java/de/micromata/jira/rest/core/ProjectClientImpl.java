package de.micromata.jira.rest.core;

import org.apache.commons.lang3.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.stream.*;
import de.micromata.jira.rest.*;
import de.micromata.jira.rest.client.*;
import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.meta.*;
import de.micromata.jira.rest.core.misc.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian
 * Date: 31.07.2014
 */
public class ProjectClientImpl extends BaseClient implements ProjectClient, RestParamConstants, RestPathConstants {
    public ProjectClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public CompletableFuture<ProjectBean> getProjectByKey(final String projectKey) {
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

    public CompletableFuture<List<ProjectBean>> getAllProjects() {
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

    public CompletableFuture<List<VersionBean>> getProjectVersions(final String projectKey) {
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

    public CompletableFuture<List<ComponentBean>> getProjectComponents(final String projectKey) {
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
    public CompletableFuture<MetaBean> getIssueTypesMetaForProject(final String projectKey) {
        Validate.notNull(projectKey);
        return submit(() -> {
            URIBuilder uriBuilder = buildPathV2(ISSUE, CREATEMETA)
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
