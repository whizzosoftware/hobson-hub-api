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
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

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
     * @param metas the class for the configuration
     *
     * @return a PropertyContainer instance containing the configuration
     */
    PropertyContainer getDeviceConfiguration(DeviceContext ctx, PropertyContainerClass metas);

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
     * Returns a device name.
     *
     * @param ctx the device context
     *
     * @return the device name
     */
    String getDeviceName(DeviceContext ctx);

    /**
     * Returns the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     *
     * @return a PropertyContainer instance containing the configuration
     */
    PropertyContainer getHubConfiguration(HubContext ctx);

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
     * @param configurationClass the associated class of the configuration
     *
     * @return a PropertyContainer containing the configuration
     */
    PropertyContainer getLocalPluginConfiguration(PluginContext ctx, PropertyContainerClass configurationClass);

    /**
     * Sets a device's configuration.
     *
     * @param ctx the device context
     * @param configClass the class for the configuration
     * @param values the configuration property values
     */
    void setDeviceConfigurationProperties(DeviceContext ctx, PropertyContainerClass configClass, Map<String, Object> values);

    /**
     * Sets a single property of a device's configuration.
     *
     * @param ctx the device context
     * @param configClass the class for the configuration
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setDeviceConfigurationProperty(DeviceContext ctx, PropertyContainerClass configClass, String name, Object value);

    /**
     * Sets a device's name.
     *
     * @param ctx the device context
     * @param name the new device name
     */
    void setDeviceName(DeviceContext ctx, String name);

    /**
     * Sets the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     * @param configuration the configuration to set
     */
    void setHubConfiguration(HubContext ctx, PropertyContainer configuration);

    /**
     * Sets the configuration for a local plugin.
     *  @param ctx the plugin context
     * @param newConfig the new configuration
     */
    void setLocalPluginConfiguration(PluginContext ctx, PropertyContainer newConfig);

    /**
     * Sets a configuration property for a local plugin.
     *  @param ctx the plugin context
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value);
}
