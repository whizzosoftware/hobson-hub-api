/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

import java.util.List;

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
    private List<ConfigurationEnumValue> enumValues;

    public ConfigurationPropertyMetaData(String id) {
        this.id = id;
    }

    /**
     * Constructor.
     *
     * @param id the ID of the property
     * @param name the human-readable name of the property
     * @param description a description of the property
     * @param type the property type
     */
    public ConfigurationPropertyMetaData(String id, String name, String description, Type type) {
        this(id, name, description, type, null);
    }

    /**
     * Constructor.
     *
     * @param id the ID of the property
     * @param name the human-readable name of the property
     * @param description a description of the property
     * @param type the property type
     * @param enumValues an list of enumerated values for the property
     */
    public ConfigurationPropertyMetaData(String id, String name, String description, Type type, List<ConfigurationEnumValue> enumValues) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.enumValues = enumValues;
    }

    /**
     * Returns the property ID.
     *
     * @return a String
     */
    public String getId() {
        return id;
    }

    /**
     * Returns a human-readable property name.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a human-readable property description.
     *
     * @return a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the property type.
     *
     * @return a Type enumeration value
     */
    public Type getType() {
        return type;
    }

    /**
     * Indicates whether this property contains enumerated values.
     *
     * @return a boolean
     */
    public boolean hasEnumValues() {
        return (enumValues != null);
    }

    /**
     * Returns this property's enumerated values.
     *
     * @return a List of ConfigurationEnumValue objects (or null if the property has no enumerated values)
     */
    public List<ConfigurationEnumValue> getEnumValues() {
        return enumValues;
    }

    public enum Type {
        BOOLEAN,
        STRING,
        PASSWORD,
        NUMBER
    }
}
