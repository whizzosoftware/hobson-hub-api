/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api;

/**
 * An unchecked exception indicating a request is invalid for some reason.
 *
 * @author Dan Noguerol
 */
public class HobsonInvalidRequestException extends HobsonRuntimeException {
    public static final int CODE = 400;

    public HobsonInvalidRequestException(String message) {
        super(CODE, message);
    }

    public HobsonInvalidRequestException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
