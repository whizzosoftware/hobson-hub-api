/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.presence;

import com.whizzosoftware.hobson.api.event.HobsonEvent;

import java.util.Map;

/**
 * Base class for all presence related events.
 *
 * @author Dan Noguerol
 */
abstract public class PresenceEvent extends HobsonEvent {
    public PresenceEvent(Long timestamp, String eventId) {
        super(timestamp, eventId);
    }

    public PresenceEvent(Map<String, Object> properties) {
        super(properties);
    }
}
