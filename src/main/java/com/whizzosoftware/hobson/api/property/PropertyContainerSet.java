/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.util.List;

/**
 * A named set of property container objects.
 *
 * @author Dan Noguerol
 */
public class PropertyContainerSet {
    private String id;
    private String name;
    private List<PropertyContainer> properties;

    public PropertyContainerSet() {
    }

    public PropertyContainerSet(String id) {
        this(id, null, null);
    }

    public PropertyContainerSet(String id, List<PropertyContainer> properties) {
        this(id, null, properties);
    }

    public PropertyContainerSet(List<PropertyContainer> properties) {
        this(null, null, properties);
    }

    public PropertyContainerSet(String id, String name, List<PropertyContainer> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
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

    public boolean hasProperties() {
        return (properties != null && properties.size() > 0);
    }

    public List<PropertyContainer> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyContainer> properties) {
        this.properties = properties;
    }
}
