/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config.manager;

import java.util.Dictionary;

/**
 * An interface used to manage plugin and device configurations.
 *
 * @author Dan Noguerol
 */
public interface ConfigurationManager {
    /**
     * Returns the plugin level configuration.
     *
     * @param pluginId the plugin ID
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Dictionary getPluginConfiguration(String pluginId);

    /**
     * Sets a plugin level configuration property.
     *
     * @param pluginId the plugin ID
     * @param name the configuration property name
     * @param value the configuration property value
     */
    public void setPluginConfigurationProperty(String pluginId, String name, Object value);

    /**
     * Returns the device level configuration.
     *
     * @param pluginId the plugin ID that owns the device
     * @param deviceId the device ID that owns the configuration
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Dictionary getDeviceConfiguration(String pluginId, String deviceId);

    /**
     * Set a device level configuration property.
     *
     * @param pluginId the plugin ID that owns the device
     * @param deviceId the device ID that owns the configuration property.
     * @param name the configuration property name
     * @param value the configuration property value
     * @param overwrite indicates whether an existing key should be overwritten
     */
    public void setDeviceConfigurationProperty(String pluginId, String deviceId, String name, Object value, boolean overwrite);

    /**
     * Allows a listener to receive a callback when a plugin's configuration changes.
     *
     * @param pluginId the plugin ID
     * @param listener the listener object to be called
     */
    public void registerForPluginConfigurationUpdates(String pluginId, PluginConfigurationListener listener);

    /**
     * Allows a listener to receive a callback when a device's configuration changes.
     *
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     * @param listener the listener object to be called
     */
    public void registerForDeviceConfigurationUpdates(String pluginId, String deviceId, DeviceConfigurationListener listener);

    /**
     * Allows a plugin to cease receiving callbacks when its configuration changes.
     *
     * @param listener the plugin that previously requested the callback
     */
    public void unregisterForConfigurationUpdates(String pluginId, PluginConfigurationListener listener);
}
