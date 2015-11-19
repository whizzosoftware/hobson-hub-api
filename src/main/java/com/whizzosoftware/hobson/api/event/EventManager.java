/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.hub.HubContext;

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
     * @param ctx the context of the hub making the request
     * @param listener the listener
     * @param topics the topics to subscribe to
     *
     * @since hobson-hub-api 0.1.6
     */
    void addListener(HubContext ctx, EventListener listener, String[] topics);

    /**
     * Unsubscribes a listener from specific event topics.
     *
     * @param ctx the context of the hub that added the listener
     * @param listener the listener
     * @param topics the topics to unsubscribe from
     */
    void removeListener(HubContext ctx, EventListener listener, String[] topics);

    /**
     * Unsubscribes a listener from all topics.
     *
     * @param ctx the context of the hub that added the listener
     * @param listener the listener to remove
     *
     * @since hobson-hub-api 0.1.6
     */
    void removeListenerFromAllTopics(HubContext ctx, EventListener listener);

    /**
     * Sends a system event.
     *
     * @param ctx the context of the hub sending the event
     * @param event the event to send
     *
     * @since hobson-hub-api 0.1.6
     */
    void postEvent(HubContext ctx, HobsonEvent event);
}
