/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;

/**
 * An exception thrown when a device is not found. This would happen, for example, when an invalid device ID is
 * requested from the device manager.
 *
 * @author Dan Noguerol
 */
public class DeviceNotFoundException extends HobsonNotFoundException {
    public DeviceNotFoundException(DeviceContext ctx) {
        super("Device " + ctx + " not found");
    }
}
