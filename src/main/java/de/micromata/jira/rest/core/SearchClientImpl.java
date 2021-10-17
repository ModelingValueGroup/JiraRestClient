package de.micromata.jira.rest.core;

import org.apache.commons.lang3.*;
import org.apache.http.client.methods.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.stream.*;
import de.micromata.jira.rest.*;
import de.micromata.jira.rest.client.*;
import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.filter.*;
import de.micromata.jira.rest.core.jql.*;
import de.micromata.jira.rest.core.misc.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 01.08.2014
 */
public class SearchClientImpl extends BaseClient implements SearchClient, RestPathConstants, RestParamConstants {
    public SearchClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public CompletableFuture<JqlSearchResult> searchIssues(final JqlSearchBean jsb) {
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

    public CompletableFuture<FilterBean> createSearchFilter(FilterBean filter) {
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

    public CompletableFuture<List<FilterBean>> getFavoriteFilter() {
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

    public CompletableFuture<FilterBean> getFilterById(String id) {
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
