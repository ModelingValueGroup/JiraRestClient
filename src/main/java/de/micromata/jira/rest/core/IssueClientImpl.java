package de.micromata.jira.rest.core;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.*;
import com.google.gson.stream.*;
import de.micromata.jira.rest.*;
import de.micromata.jira.rest.client.*;
import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.update.*;
import de.micromata.jira.rest.core.misc.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Date: ${Date}
 */
public class IssueClientImpl extends BaseClient implements IssueClient, RestParamConstants, RestPathConstants {
    public IssueClientImpl(JiraRestClient jiraRestClient) {
        super(jiraRestClient);
    }

    public CompletableFuture<IssueResponse> createIssue(final IssueBean issue) {
        Validate.notNull(issue);
        return submit(() -> {
            String json = gson.toJson(issue);
            try (CloseableHttpResponse response = executePost(json, ISSUE)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        IssueBean issueBean = gson.fromJson(jsonReader, IssueBean.class);
                        return new IssueResponse(issueBean.getKey());
                    }
                } else if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    try (InputStream inputStream = response.getEntity().getContent();
                         JsonReader jsonReader = toJsonReader(inputStream)) {
                        ErrorBean error = gson.fromJson(jsonReader, ErrorBean.class);
                        return new IssueResponse(error);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });

    }

    public CompletableFuture<IssueBean> updateIssue(final String issueKey, final IssueUpdate issueUpdate) {
        Validate.notNull(issueKey);
        Validate.notNull(issueUpdate);
        return submit(() -> {
            String json = gson.toJson(issueUpdate);
            try (CloseableHttpResponse response = executePut(json, ISSUE, issueKey)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    return getIssueByKey(issueKey).get();
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public CompletableFuture<IssueBean> getIssueByKey(final String issueKey) {
        return getIssueByKey(issueKey, null, null);
    }

    public CompletableFuture<IssueBean> getIssueByKey(final String issueKey, final List<String> fields, final List<String> expand) {
        Validate.notNull(issueKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(fields, expand, ISSUE, issueKey)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    return extractIssueBean(response);
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return null;
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public CompletableFuture<CommentsBean> getCommentsByIssue(final String issueKey) {
        Validate.notNull(issueKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(ISSUE, issueKey, COMMENT)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, CommentsBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public CompletableFuture<WorklogBean> getWorklogByIssue(final String issueKey) {
        return getWorklogByIssue(issueKey, null, null);
    }

    public CompletableFuture<WorklogBean> getWorklogByIssue(final String issueKey, final List<String> fields, final List<String> expand) {
        Validate.notNull(issueKey);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(fields, expand, ISSUE, issueKey, WORKLOG)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, WorklogBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public boolean addCommentToIssue(final String issueKey, final CommentBean comment) throws RestException, URISyntaxException, IOException {
        Validate.notNull(issueKey);
        Validate.notNull(comment);

        final String json = gson.toJson(comment);
        try (final CloseableHttpResponse response = executePost(json, ISSUE, issueKey, COMMENT)) {
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                return true;
            } else {
                throw new RestException(response);
            }
        }
    }

    public CompletableFuture<Byte[]> getAttachment(final URI uri) {
        Validate.notNull(uri);
        return submit(() -> {
            try (CloseableHttpResponse response = executeGetForFile(uri)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    return ArrayUtils.toObject(IOUtils.toByteArray(response.getEntity().getContent()));
                } else {
                    return null;
                }
            }
        });
    }

    public InputStream getAttachmentAsStream(long id) {
        return null;
    }

    public CompletableFuture<AttachmentBean> getAttachment(final long id) {
        return submit(() -> {
            try (CloseableHttpResponse response = executeGet(ATTACHMENT, String.valueOf(id))) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, AttachmentBean.class);
                    }
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    public CompletableFuture<List<AttachmentBean>> saveAttachmentToIssue(String issuekey, File... files) {
        return submit(() -> {
            URIBuilder uriBuilder = buildPathV2(ISSUE, issuekey, ATTACHMENTS);
            HttpPost   postMethod = new HttpPost(uriBuilder.build());
            postMethod.setHeader("X-Atlassian-Token", "no-check");
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (File file : files) {
                FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA);
                multipartEntityBuilder.addPart("file", fileBody);
            }
            HttpEntity entity = multipartEntityBuilder.build();
            postMethod.setEntity(entity);
            try (CloseableHttpResponse response = execute(postMethod, null)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    try (JsonReader jsonReader = getJsonReader(response)) {
                        return gson.fromJson(jsonReader, LIST_OF_ATTACHMENT);
                    }
                } else {
                    throw new RestException(response);
                }
            } finally {
                postMethod.releaseConnection();
            }
        });
    }

    public boolean transferWorklogInIssue(String issueKey, WorklogBean worklog) throws RestException, IOException, URISyntaxException {
        Validate.notNull(issueKey);
        Validate.notNull(worklog);

        String json = gson.toJson(worklog);
        try (CloseableHttpResponse response = executePost(json, ISSUE, issueKey, WORKLOG)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                return true;
            } else {
                throw new RestException(response);
            }
        }
    }

    public boolean updateIssueTransitionByKey(String issueKey, int transitionId) throws RestException, IOException, URISyntaxException {
        Validate.notNull(issueKey);

        JsonObject parent           = new JsonObject();
        JsonObject transitionObject = new JsonObject();
        transitionObject.addProperty(JsonConstants.PROP_ID, transitionId);
        parent.add(JsonConstants.ELEM_TRANSITION, transitionObject);
        String json = new Gson().toJson(parent);

        try (CloseableHttpResponse response = executePost(json, ISSUE, issueKey, TRANSITIONS)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            } else {
                throw new RestException(response);
            }
        }
    }

    public CompletableFuture<List<TransitionBean>> getIssueTransitionsByKey(final String issueKey) {
        Validate.notNull(issueKey);
        return submit(() -> {
            URIBuilder uriBuilder = buildPathV2(ISSUE, issueKey, TRANSITIONS).addParameter(EXPAND, TRANSITIONS_FIELDS);
            try (CloseableHttpResponse response = executeGet(uriBuilder)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    return extractIssueBean(response).getTransitions();
                } else {
                    throw new RestException(response);
                }
            }
        });
    }

    private IssueBean extractIssueBean(CloseableHttpResponse response) throws IOException {
        try (JsonReader jsonReader = getJsonReader(response)) {
            return gson.fromJson(jsonReader, IssueBean.class);
        }
    }
}
