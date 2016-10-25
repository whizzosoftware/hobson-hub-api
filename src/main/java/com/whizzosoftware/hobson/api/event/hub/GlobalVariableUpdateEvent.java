/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.hub;

import com.whizzosoftware.hobson.api.event.hub.HubEvent;
import com.whizzosoftware.hobson.api.variable.GlobalVariableUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalVariableUpdateEvent extends HubEvent {
    public static final String ID = "varUpdateNotify";
    public static final String PROP_UPDATES = "updates";

    public GlobalVariableUpdateEvent(long timestamp, GlobalVariableUpdate update) {
        super(timestamp, ID);

        List<GlobalVariableUpdate> updates = new ArrayList<>();
        updates.add(update);
        setProperty(PROP_UPDATES, updates);
    }

    public GlobalVariableUpdateEvent(long timestamp, List<GlobalVariableUpdate> updates) {
        super(timestamp, ID);

        setProperty(PROP_UPDATES, updates);
    }

    public GlobalVariableUpdateEvent(Map<String,Object> properties) {
        super(properties);
    }

    public List<GlobalVariableUpdate> getUpdates() {
        return (List<GlobalVariableUpdate>)getProperty(PROP_UPDATES);
    }
}
