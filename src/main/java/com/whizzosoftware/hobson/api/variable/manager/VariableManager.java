/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.manager;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.Collection;
import java.util.List;

/**
 * An interface for managing global and device variables. The variable manager is a separate entity because there are
 * variables, such as global variables, that are not associated with a particular device.
 *
 * @author Dan Noguerol
 */
public interface VariableManager {
    /**
     * Publish a new global variable.
     *
     * @param pluginId the plugin ID publishing the variable
     * @param var the variable object
     */
    public void publishGlobalVariable(String pluginId, HobsonVariable var);

    /**
     * Returns a collection of all published global variables.
     *
     * @return a Collection of HobsonVariable instances
     */
    public Collection<HobsonVariable> getGlobalVariables();

    /**
     * Unpublishes a global variable.
     *
     * @param pluginId the plugin ID that published the variable
     * @param name the variable name
     */
    public void unpublishGlobalVariable(String pluginId, String name);

    /**
     * Publish a new device variable.
     *
     * @param pluginId the plugin ID of the device publishing the variable
     * @param deviceId the device ID publishing the variable
     * @param var the variable object
     */
    public void publishDeviceVariable(String pluginId, String deviceId, HobsonVariable var);

    /**
     * Unpublishes a device variable.
     *
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     */
    public void unpublishDeviceVariable(String pluginId, String deviceId, String name);

    /**
     * Unpublishes all variables published by a device.
     *
     * @param pluginId the plugin ID of the device that published the variables
     * @param deviceId the device ID that published the variables
     */
    public void unpublishAllDeviceVariables(String pluginId, String deviceId);

    /**
     * Unpublishes all variables published by a plugin's devices.
     *
     * @param pluginId the plugin ID
     */
    public void unpublishAllPluginVariables(String pluginId);

    /**
     * Returns all published variables for a device.
     *
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID that published the variables
     *
     * @return a Collection of HobsonVariable instances
     */
    public Collection<HobsonVariable> getDeviceVariables(String pluginId, String deviceId);

    /**
     * Returns a specific published variable for a device.
     *
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @return a HobsonVariable instance (or null if not found)
     * @throws VariableNotFoundException if not found
     */
    public HobsonVariable getDeviceVariable(String pluginId, String deviceId, String name);

    /**
     * Indicates whether a device variable has been published.
     *
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @return a boolean
     */
    public boolean hasDeviceVariable(String pluginId, String deviceId, String name);

    /**
     * Sets a specific device variable.
     *
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     * @param value the variable value
     *
     * @return the last update time prior to this set call
     * @throws VariableNotFoundException if not found
     */
    public Long setDeviceVariable(String pluginId, String deviceId, String name, Object value);

    /**
     * Updates a variable and publishes an update notification.
     *
     * @param plugin the plugin firing the update
     * @param update the VariableUpdate
     */
    public void fireVariableUpdateNotification(HobsonPlugin plugin, VariableUpdate update);

    /**
     * Updates variables and publishes an update notification.
     *
     * @param plugin the plugin firing the update
     * @param updates the VariableUpdate
     */
    public void fireVariableUpdateNotifications(HobsonPlugin plugin, List<VariableUpdate> updates);
}
