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
    private List<ConfigurationProperty> properties = new ArrayList<>();

    public Configuration() {}

    public Configuration(Collection<ConfigurationProperty> properties) {
        this.properties.addAll(properties);
    }

    public Collection<ConfigurationProperty> getProperties() {
        return properties;
    }

    public Dictionary getPropertyDictionary() {
        return new Hashtable();
    }
}
