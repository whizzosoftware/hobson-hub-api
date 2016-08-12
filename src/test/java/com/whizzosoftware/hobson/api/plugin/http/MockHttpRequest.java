/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class MockHttpRequest implements HttpRequest {
    private String uri;
    private Map<String,Collection<String>> headers = new HashMap<>();
    private byte[] body;

    MockHttpRequest(URI uri) {
        this.uri = uri.toASCIIString();
    }

    String getUri() {
        return uri;
    }

    Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, Collections.singletonList(value));
    }

    @Override
    public void addHeader(String name, Collection<String> value) {
        headers.put(name, value);
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public void execute(HttpChannelCompletionHandler handler) {
        // NO-OP
    }
}
