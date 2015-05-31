/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

import java.util.Map;

/**
 * A class that encapsulates information about a hub.
 *
 * @author Dan Noguerol
 */
public class HobsonHub {
    private HubContext ctx;
    private String name;
    private String version;
    private HubConfigurationClass configurationClass = new HubConfigurationClass();
    private PropertyContainer configuration;

    public HobsonHub(HubContext ctx) {
        this.ctx = ctx;
    }

    public HubContext getContext() {
        return ctx;
    }

    public boolean hasName() {
        return (name != null);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public PropertyContainerClass getConfigurationClass() {
        return configurationClass;
    }

    public PropertyContainer getConfiguration() {
        return configuration;
    }

    public static class Builder {
        private HobsonHub hub;

        public Builder(HubContext ctx) {
            hub = new HobsonHub(ctx);
        }

        public Builder name(String name) {
            hub.name = name;
            return this;
        }

        public Builder version(String version) {
            hub.version = version;
            return this;
        }

        public Builder configuration(Map<String,Object> propertyValues) {
            hub.configuration = new PropertyContainer(null, "Hub Configuration", hub.configurationClass.getContext(), propertyValues);
            return this;
        }

        public HobsonHub build() {
            return hub;
        }
    }
}