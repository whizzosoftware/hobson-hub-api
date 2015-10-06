/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * An interface for variables that a device publishes.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface HobsonVariable {
    /**
     * Returns the ID of the device which published this variable.
     *
     * @return a device ID
     */
    String getDeviceId();

    /**
     * Returns whether this variable is read-only.
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    Mask getMask();

    /**
     * Returns the variable name.
     *
     * @return the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    String getName();

    /**
     * Returns the last time the variable was updated.
     *
     * @return a Long (or null if the variable has never been updated)
     *
     * @since hobson-hub-api 0.1.6
     */
    Long getLastUpdate();

    /**
     * Returns the ID of the plugin which published this variable.
     *
     * @return a plugin ID
     */
    String getPluginId();

    /**
     * Indicates that this variable's value should be a proxy value rather than direct value. For example, smart
     * device image and video URLs are not be returned directly; rather, a proxy URL is returned that forces those
     * media requests to route through the Hobson REST API.
     *
     * @return a boolean indicating whether a proxy should be performed
     */
    boolean hasProxyType();

    /**
     * Returns the variable proxy type.
     *
     * @return the type of proxy (or null if no proxy is necessary)
     */
    String getProxyType();

    /**
     * Returns the variable value.
     *
     * @return the variable value
     *
     * @since hobson-hub-api 0.1.6
     */
    Object getValue();

    /**
     * Indicates whether this is a global variable.
     *
     * @return a boolean
     */
    boolean isGlobal();

    enum Mask {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE
    }
}
