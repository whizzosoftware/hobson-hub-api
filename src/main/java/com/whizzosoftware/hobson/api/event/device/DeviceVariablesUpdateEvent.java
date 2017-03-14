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

import java.util.Collection;
import java.util.Collections;
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

    public DeviceVariablesUpdateEvent(long timestamp, Collection<DeviceVariableUpdate> updates) {
        super(timestamp, ID, null);
        setProperty(PROP_UPDATES, updates);
    }

    public DeviceVariablesUpdateEvent(Map<String,Object> properties) {
        super(properties);
    }

    @SuppressWarnings("unchecked")
    public Collection<DeviceVariableUpdate> getUpdates() {
        return (Collection<DeviceVariableUpdate>)getProperty(PROP_UPDATES);
    }
}
