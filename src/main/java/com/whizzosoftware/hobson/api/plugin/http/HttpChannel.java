/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.net.URI;
import java.util.Map;

/**
 * An interface that abstracts the underlying mechanism servicing HTTP requests. This is helpful in unit testing
 * HTTP calls that plugins/devices make.
 *
 * @author Dan Noguerol
 */
public interface HttpChannel {
    /**
     * Sets the plugin instance used for callbacks.
     *
     * @param plugin the plugin instance
     */
    void setPlugin(AbstractHttpClientPlugin plugin);

    /**
     * Sends an HTTP GET request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    void sendHttpGetRequest(URI uri, Map<String, String> headers, Object context);

    /**
     * Sends an HTTP POST request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    void sendHttpPostRequest(URI uri, Map<String, String> headers, byte[] data, Object context);

    /**
     * Sends an HTTP PUT request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    void sendHttpPutRequest(URI uri, Map<String, String> headers, byte[] data, Object context);

    /**
     * Sends an HTTP DELETE request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    void sendHttpDeleteRequest(URI uri, Map<String, String> headers, byte[] data, Object context);

    /**
     * Sends an HTTP PATCH request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    void sendHttpPatchRequest(URI uri, Map<String, String> headers, byte[] data, Object context);
}
