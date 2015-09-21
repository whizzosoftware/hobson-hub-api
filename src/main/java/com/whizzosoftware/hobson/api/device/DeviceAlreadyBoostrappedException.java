/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;

/**
 * An exception thrown when a device bootstrap attempt is repeated.
 *
 * @author Dan Noguerol
 */
public class DeviceAlreadyBoostrappedException extends HobsonRuntimeException {
    private String id;

    public DeviceAlreadyBoostrappedException(String id) {
        super("Device has already been bootstrapped");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
