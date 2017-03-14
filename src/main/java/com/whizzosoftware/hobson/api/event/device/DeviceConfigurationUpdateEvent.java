/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event.device;

import com.whizzosoftware.hobson.api.device.DeviceContext;

import java.util.Map;

/**
 * Event that occurs when a device's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class DeviceConfigurationUpdateEvent extends DeviceEvent {
    public static final String ID = "deviceConfigurationUpdate";

    private static final String PROP_CONFIGURATION = "configuration";

    public DeviceConfigurationUpdateEvent(long timestamp, DeviceContext dctx, Map<String,Object> configuration) {
        super(timestamp, ID, dctx);
        setProperty(PROP_CONFIGURATION, configuration);
    }

    public DeviceConfigurationUpdateEvent(Map<String,Object> props) {
        super(props);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getConfiguration() {
        return (Map<String,Object>)getProperty(PROP_CONFIGURATION);
    }
}
