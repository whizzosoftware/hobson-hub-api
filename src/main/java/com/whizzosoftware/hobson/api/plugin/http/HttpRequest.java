/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.util.Collection;

/**
 * An interface representing an HTTP request.
 *
 * @author Dan Noguerol
 */
public interface HttpRequest {
    /**
     * Adds a new request header.
     *
     * @param name the header name
     * @param value the header value
     */
    void addHeader(String name, String value);

    /**
     * Adds a new request header.
     *
     * @param name the header name
     * @param value the header values
     */
    void addHeader(String name, Collection<String> value);

    /**
     * Sets the request body.
     *
     * @param body the body content
     */
    void setBody(byte[] body);

    /**
     * Executes the request.
     *
     * @param handler the response handler
     */
    void execute(HttpChannelCompletionHandler handler);

    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        PATCH
    }
}
