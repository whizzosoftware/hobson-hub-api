/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger;

import java.util.Collection;

/**
 * An interface for managing Hobson triggers.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface TriggerManager {
    /**
     * Publishes a new TriggerProvider instance to the runtime.
     *
     * @param provider the trigger provider to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishTriggerProvider(String userId, String hubId, TriggerProvider provider);

    /**
     * Indicates whether a trigger provider has been published.
     *
     * @param pluginId the plugin ID of the trigger provider
     * @param providerId the trigger provider ID
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasTriggerProvider(String userId, String hubId, String pluginId, String providerId);

    /**
     * Returns all triggers that have been published by all trigger providers.
     *
     * @return a Collection of HobsonTrigger objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonTrigger> getAllTriggers(String userId, String hubId);

    /**
     * Returns a specific trigger.
     *
     * @param providerId the provider ID
     * @param triggerId the trigger ID
     *
     * @return a HobsonTrigger instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonTrigger getTrigger(String userId, String hubId, String providerId, String triggerId);

    /**
     * Immediately executes a specific trigger.
     *
     * @param userId
     * @param hubId
     * @param providerId
     * @param triggerId
     */
    public void executeTrigger(String userId, String hubId, String providerId, String triggerId);

    /**
     * Adds a new trigger. This should be called by trigger providers.
     *
     * @param providerId the trigger provider ID adding the trigger
     * @param trigger the trigger to add
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addTrigger(String userId, String hubId, String providerId, Object trigger);

    /**
     * Deletes a previously added trigger.
     *
     * @param providerId the trigger provider ID that added the trigger
     * @param triggerId the trigger ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTrigger(String userId, String hubId, String providerId, String triggerId);
}
