package de.micromata.jira.rest.core;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.google.gson.stream.JsonReader;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.client.SearchClient;
import de.micromata.jira.rest.core.domain.JqlSearchResult;
import de.micromata.jira.rest.core.domain.filter.FilterBean;
import de.micromata.jira.rest.core.jql.JqlSearchBean;
import de.micromata.jira.rest.core.misc.RestParamConstants;
import de.micromata.jira.rest.core.misc.RestPathConstants;
import de.micromata.jira.rest.core.util.RestException;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 01.08.2014
 */
public class SearchClientImpl extends BaseClient implements SearchClient, RestPathConstants, RestParamConstants {
    public SearchClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public Future<JqlSearchResult> searchIssues(final JqlSearchBean jsb) {
        Validate.notNull(jsb);
        return submit(() -> {
            String json = gson.toJson(jsb);
            try (CloseableHttpResponse response = executePost(json, SEARCH)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, JqlSearchResult.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });

    }

    public Future<FilterBean> createSearchFilter(FilterBean filter) {
        return submit(() -> {
            try (CloseableHttpResponse response = executePost(filter, FILTER)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, FilterBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<List<FilterBean>> getFavoriteFilter() {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(FILTER, FAVORITE)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_FILTER);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public Future<FilterBean> getFilterById(String id) {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(FILTER, id)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, FilterBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }
}
