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
     * Constructor.
     */
    public Configuration() {}

    /**
     * Constructor.
     *
     * @param dic a Dictionary to use for starting values
     */
    public Configuration(Dictionary dic) {
        Enumeration e = dic.keys();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof String) {
                String key = (String)o;
                addProperty(ConfigurationProperty.create(key, dic.get(key)));
            }
        }
    }

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
     * Returns whether the configuration has a value for a particular property.
     *
     * @param id the property ID
     *
     * @return a boolean
     */
    public boolean hasProperty(String id) {
        return (propertyMap.containsKey(id) && propertyMap.get(id) != null);
    }

    /**
     * Returns the value of a specific configuration property.
     *
     * @param id the ID of the desired property
     *
     * @return the value or null if the configuration property is not found
     */
    public Object getPropertyValue(String id) {
        ConfigurationProperty p = propertyMap.get(id);
        if (p != null) {
            return p.getValue();
        } else {
            return null;
        }
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
