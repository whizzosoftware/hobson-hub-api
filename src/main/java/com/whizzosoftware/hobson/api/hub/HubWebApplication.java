/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * Represents a local hub web application. This could be a REST API, web console, etc.
 *
 * @author Dan Noguerol
 */
public class HubWebApplication {
    private String path;
    private Class clazz;

    /**
     * Constructor.
     *
     * @param path the URI path prefix for this application
     * @param clazz the application class to instantiate
     */
    public HubWebApplication(String path, Class clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public Class getClazz() {
        return clazz;
    }
}
