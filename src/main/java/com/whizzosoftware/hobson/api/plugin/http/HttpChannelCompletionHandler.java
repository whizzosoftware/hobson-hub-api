/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An HttpChannelCompletionHandler is responsible for calling a HobsonPlugin's onHttpResponse() or
 * onHttpRequestFailure() method when an HTTP response is received.
 *
 * @author Dan Noguerol
 */
public class HttpChannelCompletionHandler {
    private final static Logger logger = LoggerFactory.getLogger(HttpChannelCompletionHandler.class);

    private AbstractHttpClientPlugin plugin;
    private Object context;

    public HttpChannelCompletionHandler(AbstractHttpClientPlugin plugin, Object context) {
        this.plugin = plugin;
        this.context = context;
    }

    public void onSuccess(final HttpResponse response) {
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.onHttpResponse(response, context);
                } catch (Throwable t) {
                    logger.error("Error processing HTTP response", t);
                }
            }
        });
    }

    public void onFailure(final Throwable t) {
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
