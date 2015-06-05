/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

/**
 * A property that specifies a type (such as STRING, NUMBER, etc).
 *
 * @author Dan Noguerol
 */
public class TypedProperty {
    private String id;
    private String name;
    private String description;
    private Type type;

    public TypedProperty(String id) {
        this.id = id;
    }

    public TypedProperty(String id, String name, String description, Type type) {
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
        COLOR,
        DATE,
        DEVICE,
        DEVICES,
        RECURRENCE,
        NUMBER,
        SECURE_STRING,
        STRING,
        TIME
    }
}
