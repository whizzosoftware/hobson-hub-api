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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * An AsyncCompletionHandler that calls a HobsonPlugin's onHttpResponse() or onHttpRequestFailure() method
 * when an HTTP response is received.
 *
 * @author Dan Noguerol
 */
public class HttpChannelCompletionHandler extends AsyncCompletionHandler<Response> {
    private final static Logger logger = LoggerFactory.getLogger(HttpChannelCompletionHandler.class);

    private AbstractHttpClientPlugin plugin;
    private Object context;

    public HttpChannelCompletionHandler(AbstractHttpClientPlugin plugin, Object context) {
        this.plugin = plugin;
        this.context = context;
    }

    @Override
    public Response onCompleted(final Response response) throws Exception {
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.onHttpResponse(response.getStatusCode(), null, response.getResponseBody(), context);
                } catch (Throwable t) {
                    logger.error("Error processing HTTP response", t);
                }
            }
        });

        return response;
    }

    @Override
    public void onThrowable(final Throwable t) {
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.onHttpRequestFailure(t, context);
                } catch (Throwable t) {
                    logger.error("Error processing HTTP failure", t);
                }
            }
        });
    }
}
