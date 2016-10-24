/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.Action;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.image.ImageInputStream;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import com.whizzosoftware.hobson.api.variable.GlobalVariable;
import com.whizzosoftware.hobson.api.variable.GlobalVariableContext;
import io.netty.util.concurrent.Future;

import java.io.File;
import java.util.Collection;

/**
 * An interface for managing Hobson plugin functions.
 *
 * Note that this interface differentiates between local plugins (i.e. plugins installed on the hub) and remote
 * plugins (i.e. plugins available from a remote repository).
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface PluginManager {
    /**
     * Add a remote repository.
     *
     * @param uri the URI of the repository
     */
    void addRemoteRepository(String uri);

    Action createAction(PropertyContainer pc);

    /**
     * Returns a File for a plugin's data directory.
     *
     * @param ctx the context of the plugin
     *
     * @return a File instance
     */
    File getDataDirectory(PluginContext ctx);


    /**
     * Returns a File for a named file located in a plugin's data directory.
     *
     * @param ctx the context of the plugin requesting the file
     * @param filename the name of the data file
     *
     * @return a File instance (or null if not found)
     */
    File getDataFile(PluginContext ctx, String filename);

    /**
     * Returns a global variable.
     *
     * @param gvctx the variable context
     *
     * @return a GlobalVariable instance
     */
    GlobalVariable getGlobalVariable(GlobalVariableContext gvctx);

    /**
     * Returns all global variables published by a plugin.
     *
     * @param pctx the plugin context
     *
     * @return a Collection of GlobalVariable instances
     */
    Collection<GlobalVariable> getGlobalVariables(PluginContext pctx);

    /**
     * Retrieves a specific plugin.
     *
     * @param ctx the context of the target plugin
     *
     * @return a HobsonPlugin instance (or null if not found)
     */
    HobsonLocalPluginDescriptor getLocalPlugin(PluginContext ctx);

    /**
     * Returns the plugin level configuration.
     *
     * @param ctx the context of the target plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    PropertyContainer getLocalPluginConfiguration(PluginContext ctx);

    /**
     * Returns the last check in time of a plugin's device.
     *
     * @param ctx the plugin context
     * @param deviceId the device ID
     *
     * @return the last check in time (or null if the device has never checked in)
     */
    Long getLocalPluginDeviceLastCheckin(PluginContext ctx, String deviceId);

    /**
     * Returns information about a plugin device variable.
     *
     * @param ctx the variable context
     *
     * @return a DeviceVariableState instance
     */
    DeviceVariableState getLocalPluginDeviceVariable(DeviceVariableContext ctx);

    /**
     * Returns a plugin's icon.
     *
     * @param ctx the context of the target plugin
     *
     * @return an ImageInputStream (or null if the plugin has no icon and no default was found)
     */
    ImageInputStream getLocalPluginIcon(PluginContext ctx);

    /**
     * Returns information about all local plugins installed on the hub.
     *
     * @param ctx the hub context
     *
     * @return a Collection of HobsonLocalPluginDescriptor instances
     */
    Collection<HobsonLocalPluginDescriptor> getLocalPlugins(HubContext ctx);

    /**
     * Retrieves descriptor for a remotely available plugin.
     *
     * @param ctx the context of the plugin
     * @param version the plugin version
     * @return a PluginDescriptor instance
     */
    HobsonPluginDescriptor getRemotePlugin(PluginContext ctx, String version);

    /**
     * Retrieve descriptors for all remotely available plugins.
     *
     * @param ctx the context of the target hub
     *
     * @return a PluginList
     */
    Collection<HobsonPluginDescriptor> getRemotePlugins(HubContext ctx);

    /**
     * Returns the remote repositories that have been enabled.
     *
     * @return a Collection of String URIs
     */
    Collection<String> getRemoteRepositories();

    /**
     * Installs a specific version of a remote plugin.
     *
     * @param ctx the context of the target plugin
     * @param pluginVersion the plugin version to install
     */
    void installRemotePlugin(PluginContext ctx, String pluginVersion);

    /**
     * Reloads the specified plugin.
     *
     * @param ctx the context of the target plugin
     */
    void reloadLocalPlugin(PluginContext ctx);

    /**
     * Removes a remote repository.
     *
     * @param uri the URI of the repository to remove
     */
    void removeRemoteRepository(String uri);

    /**
     * Sets the plugin level configuration.
     * @param ctx the context of the target plugin
     * @param config the plugin configuration
     */
    void setLocalPluginConfiguration(PluginContext ctx, PropertyContainer config);

    /**
     * Sets an individual plugin level configuration property.
     *
     * @param ctx the context of the target hub
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value);

    Future setLocalPluginDeviceVariable(DeviceVariableContext ctx, Object value);

    /**
     * Starts a plugin device.
     *
     * @param device the device proxy
     * @param name the device's name
     * @param config the device's configuration
     * @param runnable a Runnable to execute after the device has started.
     *
     * @return a Future that indicates when the device has finished starting
     */
    Future startPluginDevice(final HobsonDeviceProxy device, String name, final PropertyContainer config, final Runnable runnable);
}
