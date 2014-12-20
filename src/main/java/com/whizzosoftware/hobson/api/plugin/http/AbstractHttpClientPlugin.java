/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * An abstract base class for plugins that poll their devices using HTTP/HTTPS. It provides some convenience methods
 * for making HTTP calls asynchronously.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHttpClientPlugin extends AbstractHobsonPlugin {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClientPlugin.class);

    private CloseableHttpAsyncClient httpClient;

    public AbstractHttpClientPlugin(String pluginId) {
        super(pluginId);

        httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
    }

    /**
     * Sends an HTTP GET request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpGetRequest(URI uri, Map<String,String> headers, final Object context) {
        HttpGet get = new HttpGet(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }
        }
        sendHttpRequest(get, context);
    }

    /**
     * Sends an HTTP POST request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPostRequest(URI uri, Map<String,String> headers, byte[] data, final Object context) {
        HttpPost post = new HttpPost(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                post.setHeader(key, headers.get(key));
            }
        }
        if (data != null) {
            post.setEntity(new ByteArrayEntity(data));
        }
        sendHttpRequest(post, context);
    }

    /**
     * Sends an HTTP PUT request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPutRequest(URI uri, Map<String,String> headers, byte[] data, final Object context) {
        HttpPut put = new HttpPut(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                put.setHeader(key, headers.get(key));
            }
        }
        if (data != null) {
            put.setEntity(new ByteArrayEntity(data));
        }
        sendHttpRequest(put, context);
    }

    /**
     * Sends an HTTP DELETE request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpDeleteRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        HttpDelete delete = new HttpDelete(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                delete.setHeader(key, headers.get(key));
            }
        }
        sendHttpRequest(delete, context);
    }

    /**
     * Sends an HTTP PATCH request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPatchRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        HttpPatch patch = new HttpPatch();
        if (headers != null) {
            for (String key : headers.keySet()) {
                patch.setHeader(key, headers.get(key));
            }
        }
        if (data != null) {
            patch.setEntity(new ByteArrayEntity(data));
        }
        sendHttpRequest(patch, context);
    }

    /**
     * Callback for successful HTTP responses. Note that "successful" here refers to transport level success. For
     * example, a 500 status code response is still considered successful.
     *
     * @param statusCode the response status code
     * @param headers response headers
     * @param response the response body
     * @param context the context object passed in the request
     */
    abstract protected void onHttpResponse(int statusCode, List<Map.Entry<String,String>> headers, String response, Object context);

    /**
     * Callback for unsuccessful HTTP responses. Note that "unsuccessful" means that there was a transport level
     * problem.
     *
     * @param cause the cause of the failure
     * @param context the context object passed in the request
     */
    abstract protected void onHttpRequestFailure(Throwable cause, Object context);

    private void sendHttpRequest(HttpUriRequest request, final Object context) {
        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse response) {
                try {
                    final int statusCode = response.getStatusLine().getStatusCode();
                    final String body = EntityUtils.toString(response.getEntity());
                    executeInEventLoop(new Runnable() {
                        @Override
                        public void run() {
                            onHttpResponse(statusCode, null, body, context);
                        }
                    });
                } catch (IOException e) {
                    logger.error("Error receiving HTTP repsonse", e);
                }
            }

            @Override
            public void failed(final Exception e) {
                executeInEventLoop(new Runnable() {
                    @Override
                    public void run() {
                        onHttpRequestFailure(e, context);
                    }
                });
            }

            @Override
            public void cancelled() {
            }
        });
    }
}
