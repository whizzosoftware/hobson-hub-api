/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

/**
 * An HttpChannel implementation that uses the Ning AsyncHttpClient library.
 *
 * @author Dan Noguerol
 */
public class AsyncHttpClientChannel implements HttpChannel {
    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientChannel.class);

    private AbstractHttpClientPlugin plugin;
    private final AsyncHttpClient client;

    public AsyncHttpClientChannel(int timeoutInMillis) {
        if (timeoutInMillis > 0) {
            logger.debug("Setting HTTP request timeout to {}", timeoutInMillis);
            AsyncHttpClientConfig.Builder cf = new AsyncHttpClientConfig.Builder();
            cf.setConnectTimeout(timeoutInMillis);
            cf.setRequestTimeout(timeoutInMillis);
            client = new AsyncHttpClient(cf.build());
        } else {
            client = new AsyncHttpClient();
        }
    }

    @Override
    public void setPlugin(AbstractHttpClientPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendHttpGetRequest(URI uri, Map<String, String> headers, Object context) {
        logger.trace("Sending HTTP GET request: {}", uri.toASCIIString());
        AsyncHttpClient.BoundRequestBuilder builder = client.prepareGet(uri.toASCIIString());
        if (headers != null){
            for (String name : headers.keySet()) {
                builder.addHeader(name, headers.get(name));
            }
        }
        builder.execute(new HttpChannelCompletionHandler(plugin, context));
    }

    @Override
    public void sendHttpPostRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
        logger.trace("Sending HTTP POST request: {}", uri.toASCIIString());
        AsyncHttpClient.BoundRequestBuilder builder = client.preparePost(uri.toASCIIString());
        if (headers != null){
            for (String name : headers.keySet()) {
                builder.addHeader(name, headers.get(name));
            }
        }
        builder.setBody(data);
        builder.execute(new HttpChannelCompletionHandler(plugin, context));
    }

    @Override
    public void sendHttpPutRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
        logger.trace("Sending HTTP PUT request: {}", uri.toASCIIString());
        AsyncHttpClient.BoundRequestBuilder builder = client.preparePut(uri.toASCIIString());
        if (headers != null){
            for (String name : headers.keySet()) {
                builder.addHeader(name, headers.get(name));
            }
        }
        builder.setBody(data);
        builder.execute(new HttpChannelCompletionHandler(plugin, context));
    }

    @Override
    public void sendHttpDeleteRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
        logger.trace("Sending HTTP DELETE request: {}", uri.toASCIIString());
        AsyncHttpClient.BoundRequestBuilder builder = client.prepareDelete(uri.toASCIIString());
        if (headers != null){
            for (String name : headers.keySet()) {
                builder.addHeader(name, headers.get(name));
            }
        }
        builder.setBody(data);
        builder.execute(new HttpChannelCompletionHandler(plugin, context));
    }

    @Override
    public void sendHttpPatchRequest(URI uri, Map<String, String> headers, byte[] data, Object context) {
        throw new UnsupportedOperationException();
    }
}
