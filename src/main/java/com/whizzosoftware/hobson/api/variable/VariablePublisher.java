/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

import java.util.List;

public interface VariablePublisher {
    /**
     * Publish a new global variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishGlobalVariable(String userId, String hubId, String pluginId, String name, Object value, HobsonVariable.Mask mask);

    /**
     * Unpublishes a global variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishGlobalVariable(String userId, String hubId, String pluginId, String name);

    /**
     * Publish a new device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device publishing the variable
     * @param deviceId the device ID publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name, Object value, HobsonVariable.Mask mask);

    /**
     * Unpublishes a device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name);

    /**
     * Unpublishes all variables published by a device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variables
     * @param deviceId the device ID that published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDeviceVariables(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Unpublishes all variables published by a plugin's devices.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllPluginVariables(String userId, String hubId, String pluginId);

    /**
     * Updates a variable and publishes an update notification.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin firing the update
     * @param update the VariableUpdate
     *
     * @since hobson-hub-api 0.1.6
     */
    public void fireVariableUpdateNotification(String userId, String hubId, HobsonPlugin plugin, VariableUpdate update);

    /**
     * Updates variables and publishes an update notification.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin firing the update
     * @param updates the VariableUpdate
     *
     * @since hobson-hub-api 0.1.6
     */
    public void fireVariableUpdateNotifications(String userId, String hubId, HobsonPlugin plugin, List<VariableUpdate> updates);
}
