package de.micromata.jira.rest.core;

import org.apache.commons.lang3.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.*;
import org.apache.http.client.utils.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import de.micromata.jira.rest.*;
import de.micromata.jira.rest.core.custom.*;
import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.field.*;
import de.micromata.jira.rest.core.domain.filter.*;
import de.micromata.jira.rest.core.domain.meta.*;
import de.micromata.jira.rest.core.misc.*;
import de.micromata.jira.rest.core.util.*;

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
    public static final Type LIST_OF_ACCOUNT    = new TypeToken<ArrayList<AccountBean>>() {
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

    protected URIBuilder buildPathV2(String... paths) {
        return URIHelper.buildPath(jiraRestClient.getBaseUriV2(), paths);
    }

    protected URIBuilder buildPathV3(String... paths) {
        return URIHelper.buildPath(jiraRestClient.getBaseUriV3(), paths);
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
                throw new Wrapper("call to JIRA server " + jiraRestClient.getServerUri() + " failed: " + e.getMessage(), e);
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
        URIBuilder uriBuilder = buildPathV2(apiPath);
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
        return executePost(body, buildPathV2(apiPath));
    }

    protected CloseableHttpResponse executePost(Object body, URIBuilder uriBuilder) throws IOException, URISyntaxException {
        Validate.notNull(body);
        HttpPost              method   = HttpMethodFactory.createPostMethod(uriBuilder.build(), body.toString());
        CloseableHttpResponse response = jiraRestClient.getClient().execute(method, jiraRestClient.getClientContext());
        return new ClosableHttpResponseProxy(response, method);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CloseableHttpResponse executePut(Object body, String... apiPath) throws IOException, URISyntaxException {
        return executePut(body, buildPathV2(apiPath));
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
