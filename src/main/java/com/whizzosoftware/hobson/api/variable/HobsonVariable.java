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
     * Returns the variable name.
     *
     * @return the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public String getName();

    /**
     * Returns the variable value.
     *
     * @return the variable value
     *
     * @since hobson-hub-api 0.1.6
     */
    public Object getValue();

    /**
     * Returns whether this variable is read-only.
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public Mask getMask();

    /**
     * Returns the last time the variable was updated.
     *
     * @return a Long (or null if the variable has never been updated)
     *
     * @since hobson-hub-api 0.1.6
     */
    public Long getLastUpdate();

    public enum Mask {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE
    }
}
