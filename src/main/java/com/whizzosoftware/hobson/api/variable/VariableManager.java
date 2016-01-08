/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An interface for managing global and device variables.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface VariableManager {
    /**
     * Returns all variables published by a hub.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.5.0
     */
    Collection<HobsonVariable> getAllVariables(HubContext ctx);

    /**
     * Returns a collection of all distinct published variable names.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of Strings
     */
    Collection<String> getPublishedVariableNames(HubContext ctx);

    /**
     * Returns a collection all global variables published by a hub.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonVariable> getGlobalVariables(HubContext ctx);

    /**
     * Returns all published variables for a device.
     *
     * @param ctx the device context the variables are associated with
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonVariable> getDeviceVariables(DeviceContext ctx);

    /**
     * Returns a specific published variable.
     *
     * @param ctx the variable context
     *
     * @return a HobsonVariable instance (or null if not found)
     * @throws VariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    HobsonVariable getVariable(VariableContext ctx);

    /**
     * Indicates whether a variable has been published.
     *
     * @param ctx the variable context
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    boolean hasVariable(VariableContext ctx);

    /**
     * Publish a new variable.
     *
     * @param ctx the context of the variable
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     *
     * @since hobson-hub-api 0.1.6
     */
    void publishVariable(VariableContext ctx, Object value, HobsonVariable.Mask mask);

    /**
     * Publish a new variable.
     *
     * @param ctx the context of the variable
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     * @param mediaType indicates the type of media this variable references (or null if not applicable)
     */
    void publishVariable(VariableContext ctx, Object value, HobsonVariable.Mask mask, VariableMediaType mediaType);

    /**
     * Un-publish a variable.
     *
     * @param ctx the context of the device variable
     *
     * @since hobson-hub-api 0.1.6
     */
    void unpublishVariable(VariableContext ctx);

    /**
     * Un-publish all variables published by a device.
     *
     * @param ctx the context of the device that published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    void unpublishAllVariables(DeviceContext ctx);

    /**
     * Un-publish all variables published by a plugin and its devices.
     *
     * @param ctx the context of the plugin whose devices published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    void unpublishAllVariables(PluginContext ctx);

    /**
     * Sets a specific device variable. Note that this is an async request and the variable will not be considered
     * "set" until the device has confirmed the variable change was successfully applied (via the
     * fireVariableUpdateNotifications method).
     *
     * @param ctx the context of the variable
     * @param value the variable value
     *
     * @return the variable's last update time prior to this set call
     * @throws VariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    Long setVariable(VariableContext ctx, Object value);

    /**
     * Sets multiple device variables.
     *
     * @param ctx the context of the device that published the variables
     * @param values a Map of variable name to variable value
     *
     * @return a Map of variable name to last update time prior to this call
     *
     * @since hobson-hub-api 0.5.0
     */
    Map<String,Long> setDeviceVariables(DeviceContext ctx, Map<String,Object> values);

    /**
     * Called by a device or plugin to notify that variable updates have been successfully confirmed with the
     * hardware being controlled.
     *
     * @param ctx the context of the target hub
     * @param updates the successful variable updates
     */
    void fireVariableUpdateNotifications(HubContext ctx, List<VariableUpdate> updates);
}
