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
 * An exception thrown when a device passport attempt is repeated.
 *
 * @author Dan Noguerol
 */
public class DevicePassportAlreadyActivatedException extends HobsonRuntimeException {
    private String id;

    public DevicePassportAlreadyActivatedException(String id) {
        super("Device passport has already been activated");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
