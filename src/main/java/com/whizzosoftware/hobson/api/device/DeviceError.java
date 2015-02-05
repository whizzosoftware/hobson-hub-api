/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

/**
 * Represents a device error.
 *
 * @author Dan Noguerol
 */
public class DeviceError {
    private DeviceErrorCode code;
    private String message;

    public DeviceError(DeviceErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public DeviceErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
