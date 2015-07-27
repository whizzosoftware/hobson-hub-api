/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.util.HashMap;
import java.util.Map;

/**
 * A generic container containing a set of values for a PropertyContainerClass's supported properties. This is used for
 * hub/device configuration, task actions and task conditions.
 *
 * @author Dan Noguerol
 */
public class PropertyContainer {
    private String id;
    private String name;
    private PropertyContainerClassContext containerClassContext;
    private Map<String,Object> propertyValues;

    public PropertyContainer() {}

    public PropertyContainer(Map<String,Object> propertyValues) {
        this(null, null, null, propertyValues);
    }

    public PropertyContainer(PropertyContainerClassContext containerClassContext, Map<String,Object> propertyValues) {
        this(null, null, containerClassContext, propertyValues);
    }

    public PropertyContainer(String id, PropertyContainerClassContext containerClassContext, Map<String,Object> propertyValues) {
        this(id, null, containerClassContext, propertyValues);
    }

    public PropertyContainer(String id, String name, PropertyContainerClassContext containerClassContext, Map<String,Object> propertyValues) {
        this.id = id;
        this.name = name;
        this.containerClassContext = containerClassContext;
        this.propertyValues = propertyValues;
    }

    public boolean hasId() {
        return (id != null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyContainerClassContext getContainerClassContext() {
        return containerClassContext;
    }

    public void setContainerClassContext(PropertyContainerClassContext containerClass) {
        this.containerClassContext = containerClass;
    }

    public boolean hasPropertyValues() {
        return (propertyValues != null && propertyValues.size() > 0);
    }

    public boolean hasPropertyValue(String name) {
        return propertyValues != null && propertyValues.containsKey(name);
    }

    public Object getPropertyValue(String name) {
        return getPropertyValue(name, null);
    }

    public Object getPropertyValue(String name, Object defaultValue) {
        Object o = propertyValues != null ? propertyValues.get(name) : null;
        return o != null ? o : defaultValue;
    }

    public String getStringPropertyValue(String name) {
        return getStringPropertyValue(name, null);
    }

    public String getStringPropertyValue(String name, String defaultValue) {
        if (propertyValues != null) {
            Object o = propertyValues.get(name);
            if (o != null) {
                return o.toString();
            }
        }
        return defaultValue;
    }

    public boolean getBooleanPropertyValue(String name) {
        return getBooleanPropertyValue(name, false);
    }

    public boolean getBooleanPropertyValue(String name, boolean defaultValue) {
        if (propertyValues != null) {
            Object o = propertyValues.get(name);
            if (o != null && o instanceof Boolean) {
                return (Boolean)o;
            }
        }
        return defaultValue;
    }

    public Map<String, Object> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValue(String key, Object value) {
        if (propertyValues == null) {
            propertyValues = new HashMap<>();
        }
        propertyValues.put(key, value);
    }

    public void setPropertyValues(Map<String, Object> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
