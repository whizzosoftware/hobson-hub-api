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

import com.whizzosoftware.hobson.api.variable.DeviceVariableUpdate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An event that occurs when device variable(s) are updated.
 *
 * @author Dan Noguerol
 */
public class DeviceVariablesUpdateEvent extends DeviceEvent {
    public static final String ID = "devVarsUpdate";
    public static final String PROP_UPDATES = "updates";

    public DeviceVariablesUpdateEvent(long timestamp, DeviceVariableUpdate update) {
        this(timestamp, Collections.singletonList(update));
    }

    public DeviceVariablesUpdateEvent(long timestamp, List<DeviceVariableUpdate> updates) {
        super(timestamp, ID);
        setProperty(PROP_UPDATES, updates);
    }

    public DeviceVariablesUpdateEvent(Map<String,Object> properties) {
        super(properties);
    }

    public List<DeviceVariableUpdate> getUpdates() {
        return (List<DeviceVariableUpdate>)getProperty(PROP_UPDATES);
    }
}
