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
     * Returns the context of the variable.
     *
     * @return a VariableContext instance
     */
    DeviceVariableContext getContext();

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
     * Indicates that this variable's value references a media stream of some sort (e.g. a JPEG image or MP4 video).
     *
     * @return a boolean
     */
    boolean hasMediaType();

    /**
     * Returns the variable media type.
     *
     * @return the type of media (or null if the variable references no media)
     */
    VariableMediaType getMediaType();

    /**
     * Returns the variable value (respecting any proxy that is defined).
     *
     * @return the variable value
     *
     * @since hobson-hub-api 0.1.6
     */
    Object getValue();

    enum Mask {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE
    }
}
