/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.action.HobsonActionRef;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * A generic interface for entities that trigger Hobson actions.
 *
 * @author Dan Noguerol
 */
public interface HobsonTask {
    /**
     * Returns the task ID.
     *
     * @return a String
     */
    public String getId();

    /**
     * Returns the task provider ID.
     *
     * @return a String
     */
    public String getProviderId();

    /**
     * Returns the task name.
     *
     * @return a String
     */
    public String getName();

    /**
     * Returns the task type.
     *
     * @return a Type enum
     */
    public Type getType();

    /**
     * Returns the properties associated with this task.
     *
     * @return a Properties object
     */
    public Properties getProperties();

    /**
     * Returns whether this task has conditions associated with it.
     *
     * @return a boolean
     */
    public boolean hasConditions();

    /**
     * Returns the conditions associated with this task.
     *
     * @return a Collection of Maps
     */
    public Collection<Map<String,Object>> getConditions();

    /**
     * Returns whether this task has actions associated with it.
     *
     * @return a boolean
     */
    public boolean hasActions();

    /**
     * Returns the actions associated with this task.
     *
     * @return a Collection of HobsonActionRef objects
     */
    public Collection<HobsonActionRef> getActions();

    /**
     * Indicates whether this task is enabled.
     *
     * @return a boolean
     */
    public boolean isEnabled();

    /**
     * Immediately executes the task.
     */
    public void execute();

    public enum Type {
        EVENT,
        SCHEDULE
    }
}
