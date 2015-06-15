/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An event used to request a change to a global or device variable.
 *
 * @author Dan Noguerol
 */
public class VariableUpdateRequestEvent extends HobsonEvent {
    public static final String ID = "varUpdateReq";
    public static final String PROP_UPDATES = "updates";

    public VariableUpdateRequestEvent(long timestamp, VariableUpdate update) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        List<VariableUpdate> updates = new ArrayList<>();
        updates.add(update);
        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateRequestEvent(long timestamp, List<VariableUpdate> updates) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateRequestEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public List<VariableUpdate> getUpdates() {
        return (List<VariableUpdate>)getProperty(PROP_UPDATES);
    }
}
