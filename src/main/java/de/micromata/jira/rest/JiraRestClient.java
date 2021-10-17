package de.micromata.jira.rest;

import org.apache.commons.lang3.*;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.*;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.*;
import org.apache.http.client.utils.*;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.client.*;
import de.micromata.jira.rest.core.*;
import de.micromata.jira.rest.core.domain.field.*;
import de.micromata.jira.rest.core.misc.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 22.08.2014
 */
public class JiraRestClient implements RestParamConstants, RestPathConstants {
    private static final String                 HTTP              = "http";
    private static final String                 HTTPS             = "https";
    private static final Map<String, FieldBean> CUSTOM_FIELDS_MAP = new HashMap<>();
    private static       RequestConfig          requestConfig;

    protected final ExecutorService     executorService;
    private         URI                 serverUri;
    private         URI                 baseUriV2;
    private         URI                 baseUriV3;
    private         String              username    = StringUtils.EMPTY;
    private         CloseableHttpClient httpclient;
    private         HttpHost            proxyHost;
    @SuppressWarnings("unused")
    private final   CookieStore         cookieStore = new BasicCookieStore();
    private         HttpClientContext   clientContext;
    //
    private         IssueClient         issueClient;
    private         UserClient          userClient;
    private         SearchClient        searchClient;
    private         ProjectClient       projectClient;
    private         SystemClient        systemClient;

    public JiraRestClient(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public int connect(URI uri, String username, String password) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        return connect(uri, username, password, null);
    }

    /**
     * Builds and configures a new client connection to JIRA.
     *
     * @param uri      = the login mask URI where JIRA is running
     * @param username = login name
     * @param password = login password
     * @return 200 succees, 401 for wrong credentials and 403 for captcha is needed, you have to login at the jira website
     */
    public int connect(URI uri, String username, String password, HttpHost proxyHost) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        this.serverUri = uri;
        this.username  = username;
        String host   = uri.getHost();
        int    port   = getPort(uri.toURL());
        String scheme = HTTP;
        if (port == 443) {
            scheme = HTTPS;
        }
        HttpHost            target        = new HttpHost(host, port, scheme);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(username, password));
        httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        // Add AuthCache to the execution context
        clientContext = HttpClientContext.create();
        clientContext.setAuthCache(authCache);
        this.baseUriV2 = buildBaseURIV2(uri);
        this.baseUriV3 = buildBaseURIV3(uri);

        // setzen des Proxies
        if (proxyHost != null) {
            this.proxyHost = proxyHost;
            requestConfig  = RequestConfig.custom().setProxy(proxyHost).build();
        }

        HttpGet               method     = HttpMethodFactory.createGetMethod(URIHelper.buildPath(baseUriV2, MYSELF).build());
        CloseableHttpResponse response   = httpclient.execute(method, clientContext);
        int                   statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            // Get the Cache for the CustomFields, need to deserialize the customFields in Issue Json
            CompletableFuture<List<FieldBean>> allCustomFields = getSystemClient().getAllCustomFields();
            List<FieldBean>                    fieldBeans      = allCustomFields.get();
            for (FieldBean fieldBean : fieldBeans) {
                CUSTOM_FIELDS_MAP.put(fieldBean.getId(), fieldBean);
            }
        }
        response.close();
        return statusCode;
    }

    public static Map<String, FieldBean> getCustomFieldsMap() {
        return CUSTOM_FIELDS_MAP;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public URI getServerUri() {
        return serverUri;
    }

    /**
     * Extract port from URL
     *
     * @param endpointUrl the endpoint url
     * @return the port
     */
    private int getPort(URL endpointUrl) {
        int port = (endpointUrl.getPort() != -1 ? endpointUrl.getPort() : endpointUrl.getDefaultPort());
        if (port != -1) {
            return port;
        }
        if (HTTPS.equals(endpointUrl.getProtocol())) {
            return 443;
        }
        return 80;
    }

    private URI buildBaseURIV2(URI uri) throws URISyntaxException {
        String path = uri.getPath().replaceAll("/*$", "").concat(RestPathConstants.BASE_REST_PATH_V2);
        return new URIBuilder(uri).setPath(path).build();
    }

    private URI buildBaseURIV3(URI uri) throws URISyntaxException {
        String path = uri.getPath().replaceAll("/*$", "").concat(RestPathConstants.BASE_REST_PATH_V3);
        return new URIBuilder(uri).setPath(path).build();
    }

    public IssueClient getIssueClient() {
        if (issueClient == null) {
            issueClient = new IssueClientImpl(this);
        }
        return issueClient;
    }

    public ProjectClient getProjectClient() {
        if (projectClient == null) {
            projectClient = new ProjectClientImpl(this);
        }
        return projectClient;
    }

    public SearchClient getSearchClient() {
        if (searchClient == null) {
            searchClient = new SearchClientImpl(this);
        }
        return searchClient;
    }

    public SystemClient getSystemClient() {
        if (systemClient == null) {
            systemClient = new SystemClientImpl(this);
        }
        return systemClient;
    }

    public UserClient getUserClient() {
        if (userClient == null) {
            userClient = new UserClientImpl(this);
        }
        return userClient;
    }

    public CloseableHttpClient getClient() {
        return httpclient;
    }

    public HttpClientContext getClientContext() {
        return clientContext;
    }

    /**
     * Gets the base URI for API v2 calls.
     *
     * @return the base URI
     */
    public URI getBaseUriV2() {
        return baseUriV2;
    }

    /**
     * Gets the base URI for API v3 calls.
     *
     * @return the base URI
     */
    public URI getBaseUriV3() {
        return baseUriV3;
    }

    public String getUsername() {
        return username;
    }

    public HttpHost getProxy() {
        return proxyHost;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
