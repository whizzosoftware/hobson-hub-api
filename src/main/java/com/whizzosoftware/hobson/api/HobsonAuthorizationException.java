/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api;

/**
 * An unchecked exception that occurs when an authorization failure has occurred.
 *
 * @author Dan Noguerol
 */
public class HobsonAuthorizationException extends HobsonRuntimeException {
    public static final int CODE = 403;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public HobsonAuthorizationException(String message) {
        super(CODE, message);
    }
}
