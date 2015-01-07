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
     * Returns the plugin ID.
     *
     * @return the plugin ID
     */
    public String getId();

    /**
     * Returns the plugin name.
     *
     * @return the plugin name
     */
    public String getName();

    /**
     * Returns the plugin version string.
     *
     * @return the plugin version
     */
    public String getVersion();

    /**
     * Returns the type of plugin.
     *
     * @return a PluginType instance
     */
    public PluginType getType();

    /**
     * Returns the status of the plugin.
     *
     * @return a PluginStatus instance
     */
    public PluginStatus getStatus();

    /**
     * Indicates whether this plugin is configurable.
     *
     * @return a boolean
     */
    public boolean isConfigurable();

    /**
     * Returns the configuration property meta-data associated with this plugin.
     *
     * @return a Collection of ConfigurationPropertyMetaData objects
     */
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData();

    /**
     * Returns the topics this plugin is interested in receiving events for.
     *
     * @return an array of String topic names (or null if no events are desired)
     */
    public String[] getEventTopics();

    /**
     * Returns how often the refresh() method will be called.
     *
     * @return the refresh interval in seconds (a 0 value means never)
     */
    public long getRefreshInterval();

    /**
     * Returns a an object that can be used to invoke methods related to plugin runtime execution.
     *
     * @return a HobsonPluginExecution instance (or null if this object doesn't support a runtime)
     */
    public HobsonPluginRuntime getRuntime();
}
