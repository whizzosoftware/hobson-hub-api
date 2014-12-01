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
 * An event that occurs when device variable(s) are updated.
 *
 * @author Dan Noguerol
 */
public class VariableUpdateNotificationEvent extends HobsonEvent {
    public static final String ID = "variableUpdate";
    public static final String PROP_UPDATES = "updates";

    public VariableUpdateNotificationEvent(VariableUpdate update) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        List<VariableUpdate> updates = new ArrayList<>();
        updates.add(update);
        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateNotificationEvent(List<VariableUpdate> updates) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        setProperty(PROP_UPDATES, updates);
    }

    public VariableUpdateNotificationEvent(Map<String,Object> properties) {
        super(EventTopics.VARIABLES_TOPIC, properties);
    }

    public List<VariableUpdate> getUpdates() {
        return (List<VariableUpdate>)getProperty(PROP_UPDATES);
    }
}
