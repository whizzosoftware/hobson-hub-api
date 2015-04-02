/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.image.ImageInputStream;

import java.io.File;
import java.util.Collection;
import java.util.Map;

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
     * @param pluginId the plugin ID requesting the file
     * @param filename the name of the data file
     *
     * @return a File instance (or null if not found)
     */
    public File getDataFile(String pluginId, String filename);

    public Collection<HobsonPlugin> getAllPlugins(String userId, String hubId);

    /**
     * Retrieves a specific plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return a HobsonPlugin instance (or null if not found)
     */
    public HobsonPlugin getPlugin(String userId, String hubId, String pluginId);

    /**
     * Retrieve descriptors for all installed plugins.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param includeRemoteInfo indicates whether online Hobson plugin directory information should be included
     *
     * @return a PluginList
     */
    public PluginList getPluginDescriptors(String userId, String hubId, boolean includeRemoteInfo);

    /**
     * Returns the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(String userId, String hubId, HobsonPlugin plugin);

    /**
     * Returns a plugin's icon.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return an ImageInputStream (or null if the plugin has no icon and no default was found)
     */
    public ImageInputStream getPluginIcon(String userId, String hubId, String pluginId);

    /**
     * Returns the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(String userId, String hubId, String pluginId);

    /**
     * Returns a plugin level configuration property.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     * @param name the name of the configuration property
     *
     * @return the configuration property value (or null if not set)
     */
    public Object getPluginConfigurationProperty(String userId, String hubId, String pluginId, String name);

    /**
     * Returns the current local version of a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return the current version in x.x.x format
     */
    public String getPluginCurrentVersion(String userId, String hubId, String pluginId);

    public void publishPlugin(String userId, String hubId, HobsonPlugin plugin);

    /**
     * Installs a specific version of a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID to install
     * @param pluginVersion the plugin version to install
     */
    public void installPlugin(String userId, String hubId, String pluginId, String pluginVersion);

    /**
     * Sets the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     * @param config the plugin configuration
     */
    public void setPluginConfiguration(String userId, String hubId, String pluginId, Configuration config);

    /**
     * Sets an individual plugin level configuration property.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     * @param name the configuration property name
     * @param value the configuration property value
     */
    public void setPluginConfigurationProperty(String userId, String hubId, String pluginId, String name, Object value);

    /**
     * Reloads the specified plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID to reload
     */
    public void reloadPlugin(String userId, String hubId, String pluginId);
}
