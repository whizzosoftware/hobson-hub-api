/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClassProvider;

import java.util.List;

/**
 * A set of utility functions for dealing with tasks.
 *
 * @author Dan Noguerol
 */
public class TaskHelper {
    /**
     * Returns the trigger condition from a list of conditions.
     *
     * @param provider a task condition class provider
     * @param conditions the list of conditions
     *
     * @return a PropertyContainer instance (or null if no condition had a trigger type)
     */
    public static PropertyContainer getTriggerCondition(TaskConditionClassProvider provider, List<PropertyContainer> conditions) {
        PropertyContainer trigger = null;

        if (provider == null) {
            throw new HobsonRuntimeException("No condition class provider found");
        }

        // iterate through the conditions
        if (conditions != null) {
            for (PropertyContainer c : conditions) {
                // get the condition class for this condition
                TaskConditionClass tcc = provider.getConditionClass(c.getContainerClassContext());
                if (tcc != null) {
                    if (tcc.getConditionClassType() == ConditionClassType.trigger) {
                        if (trigger == null) {
                            trigger = c;
                        } else {
                            throw new HobsonRuntimeException("Multiple trigger conditions in a task is not supported");
                        }
                    }
                } else {
                    throw new HobsonRuntimeException("Unable to find condition class: " + c.getContainerClassContext());
                }
            }
        }

        return trigger;
    }
}
