/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

/**
 * An AsyncCompletionHandler that calls a HobsonPlugin's onHttpResponse() or onHttpRequestFailure() method
 * when an HTTP response is received.
 *
 * @author Dan Noguerol
 */
public class HttpChannelCompletionHandler extends AsyncCompletionHandler<Response> {
    private AbstractHttpClientPlugin plugin;
    private Object context;

    public HttpChannelCompletionHandler(AbstractHttpClientPlugin plugin, Object context) {
        this.plugin = plugin;
        this.context = context;
    }

    @Override
    public Response onCompleted(Response response) throws Exception {
        plugin.onHttpResponse(response.getStatusCode(), null, response.getResponseBody(), context);
        return response;
    }

    @Override
    public void onThrowable(Throwable t) {
        plugin.onHttpRequestFailure(t, context);
    }
}
