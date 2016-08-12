/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http.ning;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.plugin.http.HttpChannelCompletionHandler;
import com.whizzosoftware.hobson.api.plugin.http.HttpRequest;

import java.net.URI;
import java.util.Collection;

/**
 * A Ning-specific implementation of HttpRequest.
 *
 * @author Dan Noguerol
 */
public class NingHttpRequestAdapter implements HttpRequest {
    private final AsyncHttpClient.BoundRequestBuilder builder;

    public NingHttpRequestAdapter(AsyncHttpClient client, URI uri, Method method) {
        String suri = uri.toASCIIString();
        switch (method) {
            case GET:
                this.builder = client.prepareGet(suri);
                break;
            case PUT:
                this.builder = client.preparePut(suri);
                break;
            case POST:
                this.builder = client.preparePost(suri);
                break;
            case DELETE:
                this.builder = client.prepareDelete(suri);
                break;
            default:
                throw new HobsonRuntimeException("Unknown method: " + method);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        builder.addHeader(name, value);
    }

    @Override
    public void addHeader(String name, Collection<String> values) {
        for (String value : values) {
            builder.addHeader(name, value);
        }
    }

    @Override
    public void setBody(byte[] body) {
        builder.setBody(body);
    }

    @Override
    public void execute(final HttpChannelCompletionHandler handler) {
        builder.execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(final Response response) throws Exception {
                handler.onSuccess(new NingHttpResponseAdapter(response));
                return response;
            }

            @Override
            public void onThrowable(final Throwable t) {
                super.onThrowable(t);
                handler.onFailure(t);
            }
        });
    }
}
