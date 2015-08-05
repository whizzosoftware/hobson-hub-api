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
 * An interface for managing global and device variables. The variable manager is a separate entity because there are
 * variables, such as global variables, that are not associated with a particular device.
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
    public Collection<HobsonVariable> getAllVariables(HubContext ctx);

    public Collection<HobsonVariable> getAllVariables(HubContext ctx, VariableProxyValueProvider proxyProvider);

    public Collection<String> getPublishedVariableNames(HubContext ctx);

    /**
     * Returns a collection all global variables published by a hub.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonVariable> getGlobalVariables(HubContext ctx);

    public Collection<HobsonVariable> getGlobalVariables(HubContext ctx, VariableProxyValueProvider proxyProvider);

    /**
     * Returns a specific global variable published by a hub.
     *
     * @param ctx the context of the target hub
     * @param name the variable name
     *
     * @return a HobsonVariable instance
     * @throws VariableNotFoundException if not found
     */
    public HobsonVariable getGlobalVariable(HubContext ctx, String name);

    public HobsonVariable getGlobalVariable(HubContext ctx, String name, VariableProxyValueProvider proxyProvider);

    /**
     * Returns all published variables for a device.
     *
     * @param ctx the device context the variables are associated with
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonVariableCollection getDeviceVariables(DeviceContext ctx);

    public HobsonVariableCollection getDeviceVariables(DeviceContext ctx, VariableProxyValueProvider proxyProvider);

    /**
     * Returns a specific published variable for a device.
     *
     * @param ctx the device context the variable is associated with
     * @param name the variable name
     *
     * @return a HobsonVariable instance (or null if not found)
     * @throws VariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonVariable getDeviceVariable(DeviceContext ctx, String name);

    public HobsonVariable getDeviceVariable(DeviceContext ctx, String name, VariableProxyValueProvider proxyValueProvider);

    /**
     * Indicates whether a device variable has been published.
     *
     * @param ctx the device context the variable is associated with
     * @param name the variable name
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasDeviceVariable(DeviceContext ctx, String name);

    /**
     * Publish a new global variable.
     *
     * @param ctx the context of the plugin publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishGlobalVariable(PluginContext ctx, String name, Object value, HobsonVariable.Mask mask);

    /**
     * Publishes a new global variable.
     *
     * @param ctx the context of the plugin publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     * @param proxyType indicates the type of proxy that can perform value substitutions (or null if not applicable)
     */
    public void publishGlobalVariable(PluginContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType);

    /**
     * Un-publish a global variable.
     *
     * @param ctx the context of the plugin that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishGlobalVariable(PluginContext ctx, String name);

    /**
     * Publish a new device variable.
     *
     * @param ctx the context of the device publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask);

    /**
     * Publish a new device variable.
     *
     * @param ctx the context of the device publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     * @param proxyType indicates the type of proxy that can perform value substitutions (or null if not applicable)
     */
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType);

    /**
     * Un-publish a device variable.
     *
     * @param ctx the context of the device that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDeviceVariable(DeviceContext ctx, String name);

    /**
     * Un-publish all variables published by a device.
     *
     * @param ctx the context of the device that published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDeviceVariables(DeviceContext ctx);

    /**
     * Un-publish all variables published by a plugin's devices.
     *
     * @param ctx the context of the plugin whose devices published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllPluginVariables(PluginContext ctx);

    /**
     * Sets the value of a global variable. Note that this is an async request and the variable will not be considered
     * "set" until the plugin has confirmed the variable change was successfully applied.
     *
     * @param ctx the context of the plugin that published the global variable
     * @param name the variable name
     * @param value the variable value
     *
     * @return the variable's last update time prior to this set call
     */
    public Long setGlobalVariable(PluginContext ctx, String name, Object value);

    /**
     * Sets the value of a set of global variables. Note that this is an async request and the variable will not be
     * considered "set" until the plugin has confirmed the variable change was successfully applied.
     *
     * @param ctx the context of the plugin that published the global variable
     * @param values a map of variable name to value
     *
     * @return a map of variable name to last update time prior to this call
     */
    public Map<String,Long> setGlobalVariables(PluginContext ctx, Map<String,Object> values);

    /**
     * Sets a specific device variable. Note that this is an async request and the variable will not be considered
     * "set" until the device has confirmed the variable change was successfully applied.
     *
     * @param ctx the context of the device that published the variable
     * @param name the variable name
     * @param value the variable value
     *
     * @return the variable's last update time prior to this set call
     * @throws VariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    public Long setDeviceVariable(DeviceContext ctx, String name, Object value);

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
    public Map<String,Long> setDeviceVariables(DeviceContext ctx, Map<String,Object> values);

    /**
     * Called by a device or plugin to apply variable updates that have been successfuly confirmed with the
     * hardware being controlled.
     *
     * @param ctx the context of the target hub
     * @param updates the successful variable updates
     */
    public void applyVariableUpdates(HubContext ctx, List<VariableUpdate> updates);
}
