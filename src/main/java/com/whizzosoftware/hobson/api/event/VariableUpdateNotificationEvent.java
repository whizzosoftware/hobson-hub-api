/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import org.osgi.service.event.Event;

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

    private List<VariableUpdate> updates;

    public VariableUpdateNotificationEvent(VariableUpdate update) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        this.updates = new ArrayList<VariableUpdate>();
        this.updates.add(update);
    }

    public VariableUpdateNotificationEvent(List<VariableUpdate> updates) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        this.updates = updates;
    }

    public VariableUpdateNotificationEvent(Event event) {
        super(event);
    }

    public List<VariableUpdate> getUpdates() {
        return updates;
    }

    public void removeUpdatesWithVariableName(String name) {
        List<VariableUpdate> itemsToRemove = new ArrayList<VariableUpdate>();
    }

    @Override
    void readProperties(Event event) {
        updates = (List<VariableUpdate>)event.getProperty(PROP_UPDATES);
    }

    @Override
    void writeProperties(Map properties) {
        properties.put(PROP_UPDATES, updates);
    }
}
