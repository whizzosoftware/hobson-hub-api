/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger.manager;

import com.whizzosoftware.hobson.api.trigger.HobsonTrigger;
import com.whizzosoftware.hobson.api.trigger.TriggerProvider;

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
    public void publishTriggerProvider(TriggerProvider provider);

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
    public boolean hasTriggerProvider(String pluginId, String providerId);

    /**
     * Returns all triggers that have been published by all trigger providers.
     *
     * @return a Collection of HobsonTrigger objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonTrigger> getAllTriggers();

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
    public HobsonTrigger getTrigger(String providerId, String triggerId);

    /**
     * Adds a new trigger. This should be called by trigger providers.
     *
     * @param providerId the trigger provider ID adding the trigger
     * @param trigger the trigger to add
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addTrigger(String providerId, Object trigger);

    /**
     * Deletes a previously added trigger.
     *
     * @param providerId the trigger provider ID that added the trigger
     * @param triggerId the trigger ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTrigger(String providerId, String triggerId);
}
