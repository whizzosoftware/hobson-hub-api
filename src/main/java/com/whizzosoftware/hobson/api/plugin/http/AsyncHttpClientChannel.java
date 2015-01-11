/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.ning.http.client.AsyncHttpClient;

import java.net.URI;
import java.util.Map;

/**
 * An HttpChannel implementation that uses the Ning AsyncHttpClient library.
 *
 * @author Dan Noguerol
 */
public class AsyncHttpClientChannel implements HttpChannel {
    private AbstractHttpClientPlugin plugin;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public void setPlugin(AbstractHttpClientPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendHttpGetRequest(URI uri, Map<String, String> headers, Object context) {
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
