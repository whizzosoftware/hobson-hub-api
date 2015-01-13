/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

import java.util.*;

/**
 * Encapsulates information about a configuration. This could apply to plugins, devices, etc.
 *
 * @author Dan Noguerol
 */
public class Configuration {
    private final Map<String,ConfigurationProperty> propertyMap = new HashMap<>();

    /**
     * Returns all configuration properties.
     *
     * @return a Collection of ConfigurationProperty instances
     */
    public Collection<ConfigurationProperty> getProperties() {
        return propertyMap.values();
    }

    /**
     * Returns a specific configuration property.
     *
     * @param id the ID of the desired property
     *
     * @return a ConfigurationProperty instance (or null if not found)
     */
    public ConfigurationProperty getProperty(String id) {
        return propertyMap.get(id);
    }

    /**
     * Returns a Dictionary representation of this configuration.
     *
     * @return a Dictionary instance
     */
    public Dictionary getPropertyDictionary() {
        Hashtable<String,Object> ht = new Hashtable<>();
        for (String name : propertyMap.keySet()) {
            ConfigurationProperty cp = propertyMap.get(name);
            if (cp != null && cp.getValue() != null) {
                ht.put(name, cp.getValue());
            }
        }
        return ht;
    }

    /**
     * Returns a Map of the configuration property values.
     *
     * @return a Map of property values
     */
    public Map<String,Object> getPropertyMap() {
        Map<String,Object> map = new HashMap<>();
        for (String name : propertyMap.keySet()) {
            ConfigurationProperty cp = propertyMap.get(name);
            if (cp != null && cp.getValue() != null) {
                map.put(name, cp.getValue().toString());
            }
        }
        return map;
    }

    /**
     * Adds a property to the configuration.
     *
     * @param p the property to add
     */
    public void addProperty(ConfigurationProperty p) {
        propertyMap.put(p.getId(), p);
    }

    public String toString() {
        return propertyMap.values().toString();
    }
}
