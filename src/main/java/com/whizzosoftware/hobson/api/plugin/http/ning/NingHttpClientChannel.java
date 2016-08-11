/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http.ning;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.whizzosoftware.hobson.api.plugin.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * An HttpChannel implementation that uses the Ning AsyncHttpClient library.
 *
 * @author Dan Noguerol
 */
public class NingHttpClientChannel extends HttpClientChannel {
    private static final Logger logger = LoggerFactory.getLogger(NingHttpClientChannel.class);

    private final AsyncHttpClient client;

    public NingHttpClientChannel(int timeoutInMillis) {
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
    protected HttpRequest createHttpRequest(URI uri, HttpRequest.Method method) {
        return new NingHttpRequestAdapter(client, uri, method);
    }
}
