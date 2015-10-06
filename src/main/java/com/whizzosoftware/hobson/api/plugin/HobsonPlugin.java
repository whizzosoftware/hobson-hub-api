/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

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
    PluginContext getContext();

    /**
     * Returns the plugin name.
     *
     * @return the plugin name
     */
    String getName();

    /**
     * Returns the configuration property meta-data associated with this plugin.
     *
     * @return a PropertyContainerClass object
     */
    PropertyContainerClass getConfigurationClass();

    /**
     * Returns a an object that can be used to invoke methods related to plugin runtime execution.
     *
     * @return a HobsonPluginExecution instance (or null if this object doesn't support a runtime)
     */
    HobsonPluginRuntime getRuntime();

    /**
     * Returns the status of the plugin.
     *
     * @return a PluginStatus instance
     */
    PluginStatus getStatus();

    /**
     * Returns the type of plugin.
     *
     * @return a PluginType instance
     */
    PluginType getType();

    /**
     * Returns the plugin version string.
     *
     * @return the plugin version
     */
    String getVersion();

    /**
     * Indicates whether this plugin is configurable.
     *
     * @return a boolean
     */
    boolean isConfigurable();
}
