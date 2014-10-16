/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents meta-data about an action describing what information the action requires in order to execute
 * successfully. This information is primarily used to drive user interfaces.
 *
 * @author Dan Noguerol
 */
public class ActionMeta {
    private String id;
    private String name;
    private String description;
    private Type type;
    private List<ActionMetaEnumValue> enumValues;

    public ActionMeta(String id, String name, String description, Type type) {
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

    public void addEnumValue(ActionMetaEnumValue eValue) {
        if (enumValues == null) {
            enumValues = new ArrayList<ActionMetaEnumValue>();
        }
        enumValues.add(eValue);
    }

    public Collection<ActionMetaEnumValue> getEnumValues() {
        return enumValues;
    }

    public enum Type {
        BOOLEAN,
        NUMBER,
        STRING,
        ENUMERATION
    }
}
