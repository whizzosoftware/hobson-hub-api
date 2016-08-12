/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpClientChannelTest {
    @Test
    public void testHeadersAndCookies() throws Exception {
        MockHttpClientChannel ch = new MockHttpClientChannel();
        Map<String,String> headers = new HashMap<>();
        headers.put("Host", "foo.com");
        headers.put("Accept", "application/json");
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("foo", "bar"));
        cookies.add(new Cookie("foo2", "bar2"));
        ch.sendHttpRequest(new URI("http://www.foo.com"), HttpRequest.Method.GET, headers, cookies, null, "foo");
        MockHttpRequest req = (MockHttpRequest)ch.getLastRequest();
        assertEquals("http://www.foo.com", req.getUri());
        assertEquals(3, req.getHeaders().size());
        assertEquals("application/json", req.getHeaders().get("Accept").iterator().next());
        assertEquals("foo.com", req.getHeaders().get("Host").iterator().next());
        assertEquals("foo=bar; foo2=bar2", req.getHeaders().get("Cookie").iterator().next());
    }

    @Test
    public void testHeaderNoCookies() throws Exception {
        MockHttpClientChannel ch = new MockHttpClientChannel();
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        ch.sendHttpRequest(new URI("http://www.foo.com"), HttpRequest.Method.GET, headers, null, null, "foo");
        MockHttpRequest req = (MockHttpRequest)ch.getLastRequest();
        assertEquals("http://www.foo.com", req.getUri());
        assertEquals(1, req.getHeaders().size());
        assertEquals("application/json", req.getHeaders().get("Accept").iterator().next());
    }

    @Test
    public void testCookieNoHeaders() throws Exception {
        MockHttpClientChannel ch = new MockHttpClientChannel();
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("foo", "bar"));
        ch.sendHttpRequest(new URI("http://www.foo.com"), HttpRequest.Method.GET, null, cookies, null, "foo");
        MockHttpRequest req = (MockHttpRequest)ch.getLastRequest();
        assertEquals("http://www.foo.com", req.getUri());
        assertEquals(1, req.getHeaders().size());
        assertEquals("foo=bar", req.getHeaders().get("Cookie").iterator().next());
    }
}
