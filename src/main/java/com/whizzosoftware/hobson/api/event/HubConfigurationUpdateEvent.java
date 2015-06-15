/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import java.util.Map;

/**
 * Event that occurs when the hub's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class HubConfigurationUpdateEvent extends HobsonEvent {
    public static final String ID = "hubConfigurationUpdate";

    public HubConfigurationUpdateEvent(long timestamp) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
    }

    public HubConfigurationUpdateEvent(Map<String,Object> props) {
        super(EventTopics.STATE_TOPIC, props);
    }
}
