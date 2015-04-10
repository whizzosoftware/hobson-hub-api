/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import java.util.Collection;
import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;

/**
 * Interface for all Hobson Hub plugins.
 *
 * @author Dan Noguerol
 */
public interface HobsonPlugin {
    /**
     * Returns the context associated with this plugin.
     *
     * @return a PluginContext instance
     */
    public PluginContext getContext();

    /**
     * Returns the plugin name.
     *
     * @return the plugin name
     */
    public String getName();

    /**
     * Returns the configuration property meta-data associated with this plugin.
     *
     * @return a Collection of ConfigurationPropertyMetaData objects
     */
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData();

    /**
     * Returns a an object that can be used to invoke methods related to plugin runtime execution.
     *
     * @return a HobsonPluginExecution instance (or null if this object doesn't support a runtime)
     */
    public HobsonPluginRuntime getRuntime();

    /**
     * Returns the status of the plugin.
     *
     * @return a PluginStatus instance
     */
    public PluginStatus getStatus();

    /**
     * Returns the type of plugin.
     *
     * @return a PluginType instance
     */
    public PluginType getType();

    /**
     * Returns the plugin version string.
     *
     * @return the plugin version
     */
    public String getVersion();

    /**
     * Indicates whether this plugin is configurable.
     *
     * @return a boolean
     */
    public boolean isConfigurable();
}
