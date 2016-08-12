/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http.ning;

import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.Response;
import com.whizzosoftware.hobson.api.plugin.http.Cookie;
import com.whizzosoftware.hobson.api.plugin.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A Ning-specific implementation of HttpResponse.
 *
 * @author Dan Noguerol
 */
public class NingHttpResponseAdapter implements HttpResponse {
    private Response response;
    private Map<String,Collection<String>> headers;
    private Collection<Cookie> cookies;

    NingHttpResponseAdapter(Response response) {
        this.response = response;

        if (response.hasResponseHeaders()) {
            this.headers = new HashMap<>();
            FluentCaseInsensitiveStringsMap map = response.getHeaders();
            for (String name : map.keySet()) {
                headers.put(name, response.getHeaders(name));
            }

            List<String> c = map.get("Set-Cookie");
            if (c != null) {
                cookies = parseCookies(c);
            }
        }
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getStatusText() {
        return response.getStatusText();
    }

    @Override
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public Collection<String> getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    @Override
    public boolean hasCookies() {
        return (cookies != null);
    }

    @Override
    public Collection<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public String getBody() throws IOException {
        return response.getResponseBody();
    }

    @Override
    public InputStream getBodyAsStream() throws IOException {
        return response.getResponseBodyAsStream();
    }

    protected Collection<Cookie> parseCookies(Collection<String> headers) {
        List<Cookie> results = null;
        if (headers != null) {
            results = new ArrayList<>();
            for (String v : headers) {
                com.ning.http.client.cookie.Cookie c = com.ning.http.client.cookie.CookieDecoder.decode(v);
                results.add(new Cookie(
                    c.getName(),
                    c.getValue(),
                    c.getDomain(),
                    c.getPath(),
                    c.getMaxAge(),
                    c.isSecure(),
                    c.isHttpOnly()
                ));
            }
        }
        return results;
    }
}
