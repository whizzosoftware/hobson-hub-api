/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.manager;

import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

/**
 * An interface for sending and receiving system events.
 *
 * @author Dan Noguerol
 */
public interface EventManager {
    /**
     * Adds a listener for a specific event topic.
     *
     * @param listener the listener
     * @param topic the topic name
     */
    public void addListener(EventManagerListener listener, String topic);

    public void removeListener(EventManagerListener listener);

    /**
     * Sends a system event.
     *
     * @param event the event to send
     */
    public void postEvent(HobsonEvent event);
}
