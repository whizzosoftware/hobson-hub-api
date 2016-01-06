/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.variable.VariableChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An event that occurs when device variable(s) are updated.
 *
 * @author Dan Noguerol
 */
public class VariableUpdateNotificationEvent extends HobsonEvent {
    public static final String ID = "varUpdateNotify";
    public static final String PROP_UPDATES = "updates";

    public VariableUpdateNotificationEvent(long timestamp, VariableChange update) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        List<VariableChange> updates = new ArrayList<>();
        updates.add(update);
        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateNotificationEvent(long timestamp, List<VariableChange> updates) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateNotificationEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public List<VariableChange> getUpdates() {
        return (List<VariableChange>)getProperty(PROP_UPDATES);
    }
}
