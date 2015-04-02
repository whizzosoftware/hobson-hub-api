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
    public static final int CODE_INTERNAL_ERROR = 500;

    private int code;

    public HobsonRuntimeException(String message) {
        this(CODE_INTERNAL_ERROR, message);
    }

    /**
     * Constructor.
     *
     * @param code a machine-readable code for the exception
     * @param message the exception message
     */
    public HobsonRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause the exception cause
     */
    public HobsonRuntimeException(String message, Throwable cause) {
        this(CODE_INTERNAL_ERROR, message, cause);
    }

    /**
     * Constructor.
     *
     * @param code a machine-readable code for the exception
     * @param message the exception message
     * @param cause the exception cause
     */
    public HobsonRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Returns the machine-readable code associated with the exception.
     *
     * @return a code number
     */
    public int getCode() {
        return code;
    }
}
