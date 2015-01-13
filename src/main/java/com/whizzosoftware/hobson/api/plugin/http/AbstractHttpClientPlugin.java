/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * An abstract base class for plugins that poll their devices using HTTP/HTTPS. It provides some convenience methods
 * for making HTTP calls asynchronously.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHttpClientPlugin extends AbstractHobsonPlugin {
    private HttpChannel httpChannel;

    public AbstractHttpClientPlugin(String pluginId) {
        super(pluginId);
        this.httpChannel = new AsyncHttpClientChannel((int)(Math.max(getRefreshInterval() * 1000 / 2, 5000)));
        this.httpChannel.setPlugin(this);
    }

    public AbstractHttpClientPlugin(String pluginId, HttpChannel httpChannel) {
        super(pluginId);
        this.httpChannel = httpChannel;
        this.httpChannel.setPlugin(this);
    }

    /**
     * Sends an HTTP GET request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpGetRequest(URI uri, Map<String,String> headers, final Object context) {
        httpChannel.sendHttpGetRequest(uri, headers, context);
    }

    /**
     * Sends an HTTP POST request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPostRequest(URI uri, Map<String,String> headers, byte[] data, final Object context) {
        httpChannel.sendHttpPostRequest(uri, headers, data, context);
    }

    /**
     * Sends an HTTP PUT request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPutRequest(URI uri, Map<String,String> headers, byte[] data, final Object context) {
        httpChannel.sendHttpPutRequest(uri, headers, data, context);
    }

    /**
     * Sends an HTTP DELETE request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpDeleteRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        httpChannel.sendHttpDeleteRequest(uri, headers, data, context);
    }

    /**
     * Sends an HTTP PATCH request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    public void sendHttpPatchRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        httpChannel.sendHttpPatchRequest(uri, headers, data, context);
    }

    /**
     * Callback for successful HTTP responses. Note that "successful" here refers to transport level success. For
     * example, a 500 status code response is still considered successful.
     *
     * @param statusCode the response status code
     * @param headers response headers
     * @param response the response body
     * @param context the context object passed in the request
     */
    abstract protected void onHttpResponse(int statusCode, List<Map.Entry<String,String>> headers, String response, Object context);

    /**
     * Callback for unsuccessful HTTP responses. Note that "unsuccessful" means that there was a transport level
     * problem.
     *
     * @param cause the cause of the failure
     * @param context the context object passed in the request
     */
    abstract protected void onHttpRequestFailure(Throwable cause, Object context);

}
