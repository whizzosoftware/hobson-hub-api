/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

/**
 * Encapsulates information about a configurable property. It is comprised of two parts -- the metadata about
 * the property and its value.
 *
 * @author Dan Noguerol
 */
public class ConfigurationProperty {
    private ConfigurationPropertyMetaData metaData;
    private Object value;

    public ConfigurationProperty(ConfigurationPropertyMetaData metaData, Object value) {
        this.metaData = metaData;
        this.value = value;
    }

    public String getId() {
        return metaData.getId();
    }

    public String getName() {
        return metaData.getName();
    }

    public String getDescription() {
        return metaData.getDescription();
    }

    public ConfigurationPropertyMetaData.Type getType() {
        return metaData.getType();
    }

    public boolean hasValue() {
        return (value != null);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
