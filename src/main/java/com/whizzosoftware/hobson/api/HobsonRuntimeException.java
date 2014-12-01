/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api;

/**
 * An unchecked, Hobson-specific exception.
 *
 * @author Dan Noguerol
 */
public class HobsonRuntimeException extends RuntimeException {
    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public HobsonRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause the exception cause
     */
    public HobsonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
