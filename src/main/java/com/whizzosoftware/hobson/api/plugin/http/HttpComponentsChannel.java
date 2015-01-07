/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

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
import java.util.Map;

/**
 * An Apache HTTP Components implementation of HttpChannel.
 *
 * @author Dan Noguerol
 */
public class HttpComponentsChannel implements HttpChannel {
    private static final Logger logger = LoggerFactory.getLogger(HttpComponentsChannel.class);

    private AbstractHttpClientPlugin plugin;
    private CloseableHttpAsyncClient httpClient;

    public HttpComponentsChannel() {
        httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
    }

    public void setPlugin(AbstractHttpClientPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendHttpGetRequest(URI uri, Map<String, String> headers, Object context) {
        HttpGet get = new HttpGet(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }
        }
        sendHttpRequest(get, context);
    }

    @Override
    public void sendHttpPostRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
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

    @Override
    public void sendHttpPutRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
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

    @Override
    public void sendHttpDeleteRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
        HttpDelete delete = new HttpDelete(uri);
        if (headers != null) {
            for (String key : headers.keySet()) {
                delete.setHeader(key, headers.get(key));
            }
        }
        sendHttpRequest(delete, context);
    }

    @Override
    public void sendHttpPatchRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
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

    private void sendHttpRequest(HttpUriRequest request, final Object context) {
        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse response) {
                try {
                    final int statusCode = response.getStatusLine().getStatusCode();
                    final String body = EntityUtils.toString(response.getEntity());
                    plugin.executeInEventLoop(new Runnable() {
                        @Override
                        public void run() {
                            plugin.onHttpResponse(statusCode, null, body, context);
                        }
                    });
                } catch (IOException e) {
                    logger.error("Error receiving HTTP repsonse", e);
                }
            }

            @Override
            public void failed(final Exception e) {
                plugin.executeInEventLoop(new Runnable() {
                    @Override
                    public void run() {
                        plugin.onHttpRequestFailure(e, context);
                    }
                });
            }

            @Override
            public void cancelled() {
            }
        });
    }
}
