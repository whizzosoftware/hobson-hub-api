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
 * Represents meta-data about an action. This describes what information the action requires in order to execute
 * successfully. It is used primarily to drive user interfaces.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public class ActionMetaData {
    private String id;
    private String name;
    private String description;
    private Type type;
    private List<ActionMetaDataEnumValue> enumValues;

    /**
     * Constructor.
     *
     * @param id the metadata ID
     * @param name the metadata name
     * @param description the metadata description
     * @param type the metadata type
     */
    public ActionMetaData(String id, String name, String description, Type type) {
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

    /**
     * Adds a new enumerated value to the metadata.
     *
     * @param eValue the value to add
     */
    public void addEnumValue(ActionMetaDataEnumValue eValue) {
        if (enumValues == null) {
            enumValues = new ArrayList<>();
        }
        enumValues.add(eValue);
    }

    /**
     * Returns a list of enumerated values for the metadata.
     *
     * @return a Collection of ActionMetaDataEnumValue objects
     */
    public Collection<ActionMetaDataEnumValue> getEnumValues() {
        return enumValues;
    }

    public enum Type {
        BOOLEAN,
        NUMBER,
        STRING,
        ENUMERATION
    }
}
