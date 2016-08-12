/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * An interface representing an HTTP response.
 *
 * @author Dan Noguerol
 */
public interface HttpResponse {
    /**
     * Returns the HTTP response code.
     *
     * @return an int
     */
    int getStatusCode();

    /**
     * Returns the HTTP response text.
     *
     * @return a String
     */
    String getStatusText();

    /**
     * Indicates whether a header is present in the response.
     *
     * @param name the header name
     *
     * @return a boolean
     */
    boolean hasHeader(String name);

    /**
     * Returns a specific header in the response.
     *
     * @param name the header name
     *
     * @return a Collection of String objects (or null if the header doesn't exist)
     */
    Collection<String> getHeader(String name);

    /**
     * Returns all response headers.
     *
     * @return a Map of header name to Collection of Strings
     */
    Map<String,Collection<String>> getHeaders();

    /**
     * Indicates whether cookies were present in the response
     *
     * @return a boolean
     */
    boolean hasCookies();

    /**
     * Returns all cookies present in the response.
     *
     * @return a Collection of Cookie objects (or null if none exist)
     */
    Collection<Cookie> getCookies();

    /**
     * Returns the response body as a String.
     *
     * @return a String
     * @throws IOException on failure
     */
    String getBody() throws IOException;

    /**
     * Returns the response body as a stream.
     *
     * @return an InputStream
     * @throws IOException on failure
     */
    InputStream getBodyAsStream() throws IOException;
}
