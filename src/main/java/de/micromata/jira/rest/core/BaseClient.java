package de.micromata.jira.rest.core;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.core.custom.IssueBeanDeserializer;
import de.micromata.jira.rest.core.custom.MetaBeanDeserializer;
import de.micromata.jira.rest.core.domain.AttachmentBean;
import de.micromata.jira.rest.core.domain.ComponentBean;
import de.micromata.jira.rest.core.domain.IssueBean;
import de.micromata.jira.rest.core.domain.IssuetypeBean;
import de.micromata.jira.rest.core.domain.PriorityBean;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.StatusBean;
import de.micromata.jira.rest.core.domain.UserBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.field.FieldBean;
import de.micromata.jira.rest.core.domain.filter.FilterBean;
import de.micromata.jira.rest.core.domain.meta.MetaBean;
import de.micromata.jira.rest.core.misc.RestParamConstants;
import de.micromata.jira.rest.core.util.ClosableHttpResponseProxy;
import de.micromata.jira.rest.core.util.HttpMethodFactory;
import de.micromata.jira.rest.core.util.URIHelper;

/**
 * Author: Christian Date: 09.12.2014.
 */
public abstract class BaseClient {
    private static final boolean TRACE_JSON = Boolean.getBoolean("TRACE_JSON");
    private static final String  SEPARATOR  = ",";

    public static final Type LIST_OF_ATTACHMENT = new TypeToken<ArrayList<AttachmentBean>>() {
    }.getType();
    public static final Type LIST_OF_PROJECT    = new TypeToken<ArrayList<ProjectBean>>() {
    }.getType();
    public static final Type LIST_OF_VERSION    = new TypeToken<ArrayList<VersionBean>>() {
    }.getType();
    public static final Type LIST_OF_COMPONENT  = new TypeToken<ArrayList<ComponentBean>>() {
    }.getType();
    public static final Type LIST_OF_FILTER     = new TypeToken<ArrayList<FilterBean>>() {
    }.getType();
    public static final Type LIST_OF_ISSUETYPES = new TypeToken<ArrayList<IssuetypeBean>>() {
    }.getType();
    public static final Type LIST_OF_STATUS     = new TypeToken<ArrayList<StatusBean>>() {
    }.getType();
    public static final Type LIST_OF_PRIORITY   = new TypeToken<ArrayList<PriorityBean>>() {
    }.getType();
    public static final Type LIST_OF_FIELD      = new TypeToken<ArrayList<FieldBean>>() {
    }.getType();
    public static final Type LIST_OF_USER       = new TypeToken<ArrayList<UserBean>>() {
    }.getType();

    private final   JiraRestClient jiraRestClient;
    protected final Gson           gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(IssueBean.class, new IssueBeanDeserializer())
            .registerTypeAdapter(MetaBean.class, new MetaBeanDeserializer())
            .create();

    public BaseClient(JiraRestClient jiraRestClient) {
        this.jiraRestClient = jiraRestClient;
    }

    protected JsonReader toJsonReader(InputStream inputStream) {
        Validate.notNull(inputStream);
        InputStreamReader reader     = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonReader        jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);
        return jsonReader;
    }

    protected URIBuilder buildPath(String... paths) {
        return URIHelper.buildPath(jiraRestClient.getBaseUri(), paths);
    }

    protected JsonReader getJsonReader(CloseableHttpResponse response) throws IOException {
        InputStream inputStream = response.getEntity().getContent();
        if (TRACE_JSON) {
            inputStream = new FilterInputStream(inputStream) {
                final StringBuilder buf = new StringBuilder();

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    int read = super.read(b, off, len);
                    if (0 < read) {
                        buf.append(new String(b, off, read));
                    }
                    return read;
                }

                @Override
                public void close() throws IOException {
                    HttpRequestBase currentRequest = getMethodOf(response);
                    System.err.println("==== request ======================================================");
                    System.err.println(currentRequest == null ? "???" : currentRequest.getURI());
                    System.err.println("==== json    ======================================================");
                    System.err.println(buf);
                    System.err.println("===================================================================");
                    super.close();
                }
            };
        }
        return toJsonReader(inputStream);
    }

    protected HttpRequestBase getMethodOf(CloseableHttpResponse response) {
        return response instanceof ClosableHttpResponseProxy proxy ? proxy.getMethod() : null;
    }

    protected <T> CompletableFuture<T> submit(Callable<T> f) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return f.call();
            } catch (Exception e) {
                throw new Error("WRAPPED", e);
            }
        }, jiraRestClient.getExecutorService());
    }

    public String getLoggedInUserName() {
        return jiraRestClient.getUsername();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CloseableHttpResponse executeGet(String... apiPath) throws IOException, URISyntaxException {
        return executeGet(null, null, apiPath);
    }

    protected CloseableHttpResponse executeGet(List<String> fields, List<String> expand, String... apiPath) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = buildPath(apiPath);
        if (fields != null && !fields.isEmpty()) {
            uriBuilder.addParameter(RestParamConstants.FIELDS, String.join(SEPARATOR, fields));
        }
        if (expand != null && !expand.isEmpty()) {
            uriBuilder.addParameter(RestParamConstants.EXPAND, String.join(SEPARATOR, expand));
        }
        return executeGet(uriBuilder);
    }

    protected CloseableHttpResponse executeGet(URIBuilder uriBuilder) throws IOException, URISyntaxException {
        return executeGet(uriBuilder.build());
    }

    protected CloseableHttpResponse executeGet(URI uri) throws IOException {
        HttpGet               method   = HttpMethodFactory.createGetMethod(uri);
        CloseableHttpResponse response = jiraRestClient.getClient().execute(method, jiraRestClient.getClientContext());
        return new ClosableHttpResponseProxy(response, method);
    }

    protected CloseableHttpResponse executeGetForFile(URI uri) throws IOException {
        HttpGet               method   = HttpMethodFactory.createHttpGetForFile(uri);
        CloseableHttpResponse response = jiraRestClient.getClient().execute(method, jiraRestClient.getClientContext());
        return new ClosableHttpResponseProxy(response, method);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CloseableHttpResponse executePost(Object body, String... apiPath) throws IOException, URISyntaxException {
        return executePost(body, buildPath(apiPath));
    }

    protected CloseableHttpResponse executePost(Object body, URIBuilder uriBuilder) throws IOException, URISyntaxException {
        Validate.notNull(body);
        HttpPost              method   = HttpMethodFactory.createPostMethod(uriBuilder.build(), body.toString());
        CloseableHttpResponse response = jiraRestClient.getClient().execute(method, jiraRestClient.getClientContext());
        return new ClosableHttpResponseProxy(response, method);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CloseableHttpResponse executePut(Object body, String... apiPath) throws IOException, URISyntaxException {
        return executePut(body, buildPath(apiPath));
    }

    protected CloseableHttpResponse executePut(Object body, URIBuilder uriBuilder) throws IOException, URISyntaxException {
        Validate.notNull(body);
        HttpPut               method   = HttpMethodFactory.createPutMethod(uriBuilder.build(), body.toString());
        CloseableHttpResponse response = jiraRestClient.getClient().execute(method, jiraRestClient.getClientContext());
        return new ClosableHttpResponseProxy(response, method);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CloseableHttpResponse execute(HttpPost method, HttpClientContext context) throws IOException {
        return jiraRestClient.getClient().execute(method, context);
    }
}
