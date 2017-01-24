/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.config;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.io.NotSerializableException;
import java.util.Map;

/**
 * An interface for classes that can store/retrieve configuration for hubs, plugins and devices.
 *
 * @author Dan Noguerol
 */
public interface ConfigurationManager {
    String HOBSON_HOME = "hobson.home";

    /**
     * Deletes the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     */
    void deleteHubConfiguration(HubContext ctx);

    /**
     * Retrieves a specific device's configuration.
     *
     * @param ctx the device context
     *
     * @return a PropertyContainer instance containing the configuration
     */
    Map<String,Object> getDeviceConfiguration(DeviceContext ctx);

    /**
     * Retrieves a specific device's configuration property.
     *
     * @param ctx the device context
     * @param name the property name
     *
     * @return the property value
     */
    Object getDeviceConfigurationProperty(DeviceContext ctx, String name);

    /**
     * Returns the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     *
     * @return a PropertyContainer instance containing the configuration
     */
    Map<String,Object> getHubConfiguration(HubContext ctx);

    /**
     * Returns a configuration property associated with a Hub.
     *
     * @param ctx the context of the hub
     * @param name the name of the property
     *
     * @return the property value
     */
    Object getHubConfigurationProperty(HubContext ctx, String name);

    /**
     * Returns the configuration for a local plugin.
     *
     * @param ctx the plugin context
     *
     * @return a PropertyContainer containing the configuration
     */
    Map<String,Object> getLocalPluginConfiguration(PluginContext ctx);

    /**
     * Sets a device's configuration.
     *
     * @param ctx the device context
     * @param values the configuration property values
     */
    void setDeviceConfigurationProperties(DeviceContext ctx, Map<String, Object> values) throws NotSerializableException;

    /**
     * Sets a single property of a device's configuration.
     *
     * @param ctx the device context
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value) throws NotSerializableException;

    /**
     * Sets the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     * @param config the configuration to set
     */
    void setHubConfiguration(HubContext ctx, Map<String,Object> config) throws NotSerializableException;

    /**
     * Sets the configuration for a local plugin.
     *  @param ctx the plugin context
     * @param config the new configuration
     */
    void setLocalPluginConfiguration(PluginContext ctx, Map<String,Object> config) throws NotSerializableException;

    /**
     * Sets a configuration property for a local plugin.
     *  @param ctx the plugin context
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value) throws NotSerializableException;
}
