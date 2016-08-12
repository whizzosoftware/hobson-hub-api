/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

/**
 * A class representing an HTTP cookie.
 *
 * @author Dan Noguerol
 */
public class Cookie {
    private String name;
    private String value;
    private String domain;
    private String path;
    private Integer maxAge;
    private Boolean secure;
    private Boolean httpOnly;

    public Cookie(String name, String value) {
        this(name, value, null, null, null, null, null);
    }

    public Cookie(String name, String value, String domain, String path, Integer maxAge, Boolean secure, Boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.maxAge = maxAge;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public Boolean getSecure() {
        return secure;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }
}
