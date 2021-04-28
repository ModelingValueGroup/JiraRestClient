package de.micromata.jira.rest.core;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;

import com.google.gson.stream.JsonReader;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.client.UserClient;
import de.micromata.jira.rest.core.domain.UserBean;
import de.micromata.jira.rest.core.domain.permission.MyPermissionsBean;
import de.micromata.jira.rest.core.misc.RestParamConstants;
import de.micromata.jira.rest.core.misc.RestPathConstants;
import de.micromata.jira.rest.core.util.RestException;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 02.08.2014
 */
public class UserClientImpl extends BaseClient implements UserClient, RestPathConstants, RestParamConstants {
    public UserClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public CompletableFuture<List<UserBean>> getAssignableUserForProject(String projectKey, Integer startAt, Integer maxResults) {
        return getAssignableSearch(null, null, projectKey, startAt, maxResults);
    }

    public CompletableFuture<List<UserBean>> getAssignableUsersForIssue(String issueKey, Integer startAt, Integer maxResults) {
        return getAssignableSearch(null, issueKey, null, startAt, maxResults);
    }

    public CompletableFuture<UserBean> getUserByUsername(final String username) {
        Validate.notNull(username);
        return submit(() -> {
            URIBuilder uriBuilder = buildPath(USER);
            uriBuilder.addParameter(USERNAME, username);
            try (CloseableHttpResponse response = executeGet(uriBuilder)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, UserBean.class);
                    }
                } else if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED || statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    return null;
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public CompletableFuture<UserBean> getLoggedInRemoteUser() {
        return getUserByUsername(getLoggedInUserName());
    }

    @Override
    public CompletableFuture<MyPermissionsBean> getMyPermissions() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(MYPERMISSIONS)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, MyPermissionsBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    private CompletableFuture<List<UserBean>> getAssignableSearch(@SuppressWarnings("SameParameterValue") final String username, final String issueKey, final String projectKey, final Integer startAt, final Integer maxResults) {
        return submit(() -> {

            URIBuilder uriBuilder = buildPath(USER, ASSIGNABLE, SEARCH);
            if (StringUtils.trimToNull(username) != null) {
                uriBuilder.addParameter(USERNAME, username);
            }
            if (StringUtils.trimToNull(issueKey) != null) {
                uriBuilder.addParameter(ISSUEKEY, issueKey);
            }
            if (StringUtils.trimToNull(projectKey) != null) {
                uriBuilder.addParameter(PROJECTKEY, projectKey);
            }
            if (startAt != null && startAt >= 0) {
                uriBuilder.addParameter(START_AT, startAt.toString());
            }
            if (maxResults != null && maxResults > 0 && maxResults < 1000) {
                uriBuilder.addParameter(MAX_RESULTS, maxResults.toString());
            }
            try (CloseableHttpResponse response = executeGet(uriBuilder)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_USER);
                    }
                } else if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED || statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    return new ArrayList<>();
                } else {
                    throw new RestException(response);
                }
            }
        });
    }
}
