/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.net.URI;

class MockHttpClientChannel extends HttpClientChannel {
    private HttpRequest request;

    @Override
    protected HttpRequest createHttpRequest(URI uri, HttpRequest.Method method) {
        request = new MockHttpRequest(uri);
        return request;
    }

    HttpRequest getLastRequest() {
        return request;
    }
}
