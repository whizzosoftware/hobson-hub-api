/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;

/**
 * Exception thrown when an attempt is made to access a variable that has not been published.
 *
 * @author Dan Noguerol
 */
public class VariableNotFoundException extends HobsonNotFoundException {
    public VariableNotFoundException(String message) {
        super(message);
    }

    public VariableNotFoundException(DeviceContext ctx, String name) {
        this(ctx.getHubContext(), ctx.getPluginId(), ctx.getDeviceId(), name);
    }

    public VariableNotFoundException(HubContext ctx, String pluginId, String deviceId, String name) {
        super("Variable " + ctx + HubContext.DELIMITER + pluginId + HubContext.DELIMITER + deviceId + "[" + name + "]" + " not found");
    }
}
