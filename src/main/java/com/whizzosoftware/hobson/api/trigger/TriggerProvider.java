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
 * Interface for classes that can contribute triggers to the system.
 *
 * @author Dan Noguerol
 */
public interface TriggerProvider {
    /**
     * Returns all triggers for this provider.
     *
     * @return a Collection of HobsonTrigger instances
     */
    public Collection<HobsonTrigger> getTriggers();

    /**
     * Returns a specific trigger.
     *
     * @param triggerId the trigger ID
     *
     * @return a HobsonTrigger instance (or null if not found)
     */
    public HobsonTrigger getTrigger(String triggerId);

    /**
     * Adds a new trigger.
     *
     * @param trigger the trigger
     */
    public void addTrigger(Object trigger);

    /**
     * Updates an existing trigger.
     *
     * @param triggerId the ID of the trigger
     * @param name the new trigger name
     * @param data the trigger data
     */
    public void updateTrigger(String triggerId, String name, Object data);

    /**
     * Removes an existing trigger.
     *
     * @param triggerId the ID of the trigger
     */
    public void deleteTrigger(String triggerId);
}
