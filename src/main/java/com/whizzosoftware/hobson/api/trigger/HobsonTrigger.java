/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger;

import com.whizzosoftware.hobson.api.action.HobsonActionRef;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * A generic interface for entities that trigger Hobson actions.
 *
 * @author Dan Noguerol
 */
public interface HobsonTrigger {
    /**
     * Returns the trigger ID.
     *
     * @return a String
     */
    public String getId();

    /**
     * Returns the trigger provider ID.
     *
     * @return a String
     */
    public String getProviderId();

    /**
     * Returns the trigger name.
     *
     * @return a String
     */
    public String getName();

    /**
     * Returns the trigger type.
     *
     * @return a Type enum
     */
    public Type getType();

    public Properties getProperties();

    public boolean hasConditions();

    public Collection<Map<String,Object>> getConditions();

    public boolean hasActions();

    public Collection<HobsonActionRef> getActions();

    /**
     * Indicates whether this trigger is enabled.
     *
     * @return a boolean
     */
    public boolean isEnabled();

    /**
     * Immediately executes the trigger.
     */
    public void execute();

    public enum Type {
        EVENT,
        SCHEDULE
    }
}
