/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * An abstract class that handles building and executing HTTP requests.
 *
 * @author Dan Noguerol
 */
abstract public class HttpClientChannel {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientChannel.class);

    private AbstractHttpClientPlugin plugin;

    public void setPlugin(AbstractHttpClientPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendHttpRequest(URI uri, HttpRequest.Method method, Map<String, String> headers, Collection<Cookie> cookies, byte[] body, Object context) {
        logger.trace("Sending HTTP {} request: {}", method, uri.toASCIIString());
        HttpRequest request = createHttpRequest(uri, method);
        buildHeaders(request, headers, cookies);
        request.setBody(body);
        request.execute(new HttpChannelCompletionHandler(plugin, context));
    }

    abstract protected HttpRequest createHttpRequest(URI uri, HttpRequest.Method method);

    abstract protected WebSocketHandle createWebSocket(URI uri, Map<String,Collection<String>> headers);

    private void buildHeaders(HttpRequest request, Map<String,String> headers, Collection<Cookie> cookies) {
        if (headers != null){
            for (String name : headers.keySet()) {
                request.addHeader(name, headers.get(name));
            }
        }
        if (cookies != null && cookies.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator<Cookie> it = cookies.iterator();
            while (it.hasNext()) {
                Cookie c = it.next();
                sb.append(c.getName()).append("=").append(c.getValue());
                if (it.hasNext()) {
                    sb.append("; ");
                }
            }
            request.addHeader("Cookie", sb.toString());
        }
    }
}
