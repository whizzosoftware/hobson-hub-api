/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.image.ImageInputStream;

import java.io.File;
import java.util.Collection;

/**
 * An interface for managing Hobson plugins.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface PluginManager {
    /**
     * Returns a data file for a specific plugin.
     *
     * @param ctx the context of the plugin requesting the file
     * @param filename the name of the data file
     *
     * @return a File instance (or null if not found)
     */
    public File getDataFile(PluginContext ctx, String filename);

    /**
     * Returns all plugins published by a specific hub.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonPlugin instances
     */
    public Collection<HobsonPlugin> getAllPlugins(HubContext ctx);

    /**
     * Retrieves a specific plugin.
     *
     * @param ctx the context of the target plugin
     *
     * @return a HobsonPlugin instance (or null if not found)
     */
    public HobsonPlugin getPlugin(PluginContext ctx);

    /**
     * Retrieve descriptors for all locally installed plugins.
     *
     * @param ctx the context of the target hub
     *
     * @return a PluginList
     */
    public Collection<PluginDescriptor> getLocalPluginDescriptors(HubContext ctx);

    /**
     * Retrieve descriptors for all remotely available plugins.
     *
     * @param ctx the context of the target hub
     *
     * @return a PluginList
     */
    public Collection<PluginDescriptor> getRemotePluginDescriptors(HubContext ctx);

    /**
     * Returns the plugin level configuration.
     *
     * @param plugin the plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(HobsonPlugin plugin);

    /**
     * Returns a plugin's icon.
     *
     * @param ctx the context of the target plugin
     *
     * @return an ImageInputStream (or null if the plugin has no icon and no default was found)
     */
    public ImageInputStream getPluginIcon(PluginContext ctx);

    /**
     * Returns the plugin level configuration.
     *
     * @param ctx the context of the target plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(PluginContext ctx);

    /**
     * Returns a plugin level configuration property.
     *
     * @param ctx the context of the target plugin
     * @param name the name of the configuration property
     *
     * @return the configuration property value (or null if not set)
     */
    public Object getPluginConfigurationProperty(PluginContext ctx, String name);

    /**
     * Returns the current local version of a plugin.
     *
     * @param ctx the context of the target plugin
     *
     * @return the current version in x.x.x format
     */
    public String getPluginCurrentVersion(PluginContext ctx);

    /**
     * Publishes a plugin.
     *
     * @param plugin the plugin to publish
     */
    public void publishPlugin(HobsonPlugin plugin);

    /**
     * Installs a specific version of a plugin.
     *
     * @param ctx the context of the target plugin
     * @param pluginVersion the plugin version to install
     */
    public void installPlugin(PluginContext ctx, String pluginVersion);

    /**
     * Sets the plugin level configuration.
     *
     * @param ctx the context of the target plugin
     * @param config the plugin configuration
     */
    public void setPluginConfiguration(PluginContext ctx, Configuration config);

    /**
     * Sets an individual plugin level configuration property.
     *
     * @param ctx the context of the target hub
     * @param name the configuration property name
     * @param value the configuration property value
     */
    public void setPluginConfigurationProperty(PluginContext ctx, String name, Object value);

    /**
     * Reloads the specified plugin.
     *
     * @param ctx the context of the target plugin
     */
    public void reloadPlugin(PluginContext ctx);
}
