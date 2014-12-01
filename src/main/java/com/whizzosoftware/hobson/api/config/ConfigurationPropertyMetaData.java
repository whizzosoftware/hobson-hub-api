/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

/**
 * Meta data about a configurable property.
 *
 * @author Dan Noguerol
 */
public class ConfigurationPropertyMetaData {
    private String id;
    private String name;
    private String description;
    private Type type;

    public ConfigurationPropertyMetaData(String id) {
        this.id = id;
    }

    public ConfigurationPropertyMetaData(String id, String name, String description, Type type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BOOLEAN,
        STRING,
        PASSWORD,
        NUMBER
    }
}
