/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.List;

/**
 * An interface for sending and receiving system events.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface EventManager {
    /**
     * Subscribes a listener to a specific event topic.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param listener the listener
     * @param topics the topics to subscribe to
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addListener(String userId, String hubId, EventListener listener, String[] topics);

    /**
     * Unsubscribes a listener from specific event topics.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param listener the listener
     * @param topics the topics to unsubscribe from
     */
    public void removeListener(String userId, String hubId, EventListener listener, String[] topics);

    /**
     * Unsubscribes a listener from all topics.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param listener the listener to remove
     *
     * @since hobson-hub-api 0.1.6
     */
    public void removeListenerFromAllTopics(String userId, String hubId, EventListener listener);

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
