package de.micromata.jira.rest.core.util;

import org.apache.commons.codec.*;
import org.apache.http.HttpHeaders;
import org.apache.http.*;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.message.*;

import java.net.*;

import javax.ws.rs.core.*;

import de.micromata.jira.rest.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 25.08.2014
 */
public class HttpMethodFactory {
    public static HttpGet createGetMethod(URI uri) {
        if (uri == null) {
            return null;
        }
        HttpGet method = new HttpGet(uri);
        setHeader(method);
        configProxy(method);
        return method;
    }


    public static HttpGet createHttpGetForFile(URI uri) {
        if (uri == null) {
            return null;
        }
        HttpGet method = new HttpGet(uri);
        configProxy(method);
        method.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM);
        return method;
    }

    public static HttpPost createPostMethod(URI uri, String body) {
        if (uri == null) {
            return null;
        }
        HttpPost method = new HttpPost(uri);
        setHeader(method);
        configProxy(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }

    public static HttpPut createPutMethod(URI uri, String body) {
        if (uri == null) {
            return null;
        }
        HttpPut method = new HttpPut(uri);
        setHeader(method);
        configProxy(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }

    private static void setHeader(HttpMessage httpMessage) {
        httpMessage.addHeader(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
        httpMessage.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    private static void configProxy(HttpRequestBase method) {
        RequestConfig requestConfig = JiraRestClient.getRequestConfig();
        if (requestConfig != null) {
            method.setConfig(requestConfig);
        }
    }
}
