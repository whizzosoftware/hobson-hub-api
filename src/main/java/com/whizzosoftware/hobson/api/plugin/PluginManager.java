/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.image.ImageInputStream;
import com.whizzosoftware.hobson.api.property.PropertyContainer;

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
     * Add a remote repository.
     *
     * @param uri the URI of the repository
     */
    public void addRemoteRepository(String uri);

    /**
     * Returns a File for a plugin's data directory.
     *
     * @param ctx the context of the plugin
     */
    public File getDataDirectory(PluginContext ctx);

    /**
     * Returns a File for a named file located in a plugin's data directory.
     *
     * @param ctx the context of the plugin requesting the file
     * @param filename the name of the data file
     *
     * @return a File instance (or null if not found)
     */
    public File getDataFile(PluginContext ctx, String filename);

    /**
     * Retrieves a specific plugin.
     *
     * @param ctx the context of the target plugin
     *
     * @return a HobsonPlugin instance (or null if not found)
     */
    public HobsonPlugin getLocalPlugin(PluginContext ctx);

    /**
     * Returns the plugin level configuration.
     *
     * @param ctx the context of the target plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public PropertyContainer getLocalPluginConfiguration(PluginContext ctx);

    /**
     * Returns a plugin level configuration property.
     *
     * @param ctx the context of the target plugin
     * @param name the name of the configuration property
     *
     * @return the configuration property value (or null if not set)
     */
    public Object getLocalPluginConfigurationProperty(PluginContext ctx, String name);

    /**
     * Returns a plugin's icon.
     *
     * @param ctx the context of the target plugin
     *
     * @return an ImageInputStream (or null if the plugin has no icon and no default was found)
     */
    public ImageInputStream getLocalPluginIcon(PluginContext ctx);

    /**
     * Retrieve descriptors for all locally installed plugins.
     *
     * @param ctx the context of the target hub
     *
     * @return a PluginList
     */
    public Collection<PluginDescriptor> getLocalPluginDescriptors(HubContext ctx);

    /**
     * Retrieves descriptor for a remotely available plugin.
     *
     * @param ctx the context of the plugin
     * @param version the plugin version
     * @return
     */
    public PluginDescriptor getRemotePluginDescriptor(PluginContext ctx, String version);

    /**
     * Retrieve descriptors for all remotely available plugins.
     *
     * @param ctx the context of the target hub
     *
     * @return a PluginList
     */
    public Collection<PluginDescriptor> getRemotePluginDescriptors(HubContext ctx);

    /**
     * Returns the remote repositories that have been enabled.
     *
     * @return a Collection of String URIs
     */
    public Collection<String> getRemoteRepositories();

    /**
     * Installs a specific version of a remote plugin.
     *
     * @param ctx the context of the target plugin
     * @param pluginVersion the plugin version to install
     */
    public void installRemotePlugin(PluginContext ctx, String pluginVersion);

    /**
     * Reloads the specified plugin.
     *
     * @param ctx the context of the target plugin
     */
    public void reloadLocalPlugin(PluginContext ctx);

    /**
     * Removes a remote repository.
     *
     * @param uri the URI of the repository to remove
     */
    public void removeRemoteRepository(String uri);

    /**
     * Sets the plugin level configuration.
     *  @param ctx the context of the target plugin
     * @param config the plugin configuration
     */
    public void setLocalPluginConfiguration(PluginContext ctx, PropertyContainer config);

    /**
     * Sets an individual plugin level configuration property.
     *
     * @param ctx the context of the target hub
     * @param name the configuration property name
     * @param value the configuration property value
     */
    public void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value);
}
