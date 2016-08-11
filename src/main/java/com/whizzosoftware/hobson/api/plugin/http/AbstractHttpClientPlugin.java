/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.http.ning.NingHttpClientChannel;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * An abstract base class for plugins that poll their devices using HTTP/HTTPS. It provides some convenience methods
 * for making HTTP calls asynchronously.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHttpClientPlugin extends AbstractHobsonPlugin {
    private HttpClientChannel httpChannel;

    public AbstractHttpClientPlugin(String pluginId) {
        super(pluginId);
        // use Ning implementation by default
        this.httpChannel = new NingHttpClientChannel((int)(Math.max(getRefreshInterval() * 1000 / 2, 5000)));
        this.httpChannel.setPlugin(this);
    }

    public AbstractHttpClientPlugin(String pluginId, HttpClientChannel httpChannel) {
        super(pluginId);
        this.httpChannel = httpChannel;
        this.httpChannel.setPlugin(this);
    }

    /**
     * Sends an HTTP request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param method the request method
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpRequest(URI uri, HttpRequest.Method method, final Object context) {
        sendHttpRequest(uri, method, null, null, null, context);
    }

    /**
     * Sends an HTTP request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param method the request method
     * @param headers request headers (or null if none)
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpRequest(URI uri, HttpRequest.Method method, Map<String,String> headers, final Object context) {
        sendHttpRequest(uri, method, headers, null, null, context);
    }

    /**
     * Sends an HTTP request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param method the request method
     * @param headers request headers (or null if none)
     * @param cookies cookies to include in the request (or null if none)
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpRequest(URI uri, HttpRequest.Method method, Map<String,String> headers, Collection<Cookie> cookies, final Object context) {
        sendHttpRequest(uri, method, headers, cookies, null, context);
    }

    /**
     * Sends an HTTP request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param method the request method
     * @param headers request headers (or null if none)
     * @param cookies cookies to include in the request (or null if none)
     * @param body the response body
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpRequest(URI uri, HttpRequest.Method method, Map<String,String> headers, Collection<Cookie> cookies, byte[] body, final Object context) {
        httpChannel.sendHttpRequest(uri, method, headers, cookies, body, context);
    }

    /**
     * Callback for successful HTTP responses. Note that "successful" here refers to transport level success. For
     * example, a 500 status code response is still considered successful.
     *
     * @param response the response object
     * @param context the context object passed in the request
     */
    abstract public void onHttpResponse(HttpResponse response, Object context);

    /**
     * Callback for unsuccessful HTTP responses. Note that "unsuccessful" means that there was a transport level
     * problem.
     *
     * @param cause the cause of the failure
     * @param context the context object passed in the request
     */
    abstract public void onHttpRequestFailure(Throwable cause, Object context);

}
