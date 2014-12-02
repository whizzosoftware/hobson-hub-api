/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import java.util.Collection;

/**
 * An interface for sending and receiving system events.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface EventManager {
    /**
     * Adds a listener for a specific event topic.
     *
     * @param listener the listener
     * @param topics the topics to subscribe to
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addListener(EventListener listener, Collection<String> topics);

    /**
     * Removes a previously added listener.
     *
     * @param listener the listener to remove
     *
     * @since hobson-hub-api 0.1.6
     */
    public void removeListener(EventListener listener);

    /**
     * Sends a system event.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param event the event to send
     *
     * @since hobson-hub-api 0.1.6
     */
    public void postEvent(String userId, String hubId, HobsonEvent event);
}
