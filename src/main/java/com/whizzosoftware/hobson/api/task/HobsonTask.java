/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import com.whizzosoftware.hobson.api.task.action.TaskActionClassProvider;

import java.util.*;

/**
 * A generic interface for entities that trigger Hobson actions.
 *
 * @author Dan Noguerol
 */
public class HobsonTask {
    private TaskContext context;
    private String name;
    private String description;
    private Map<String,Object> properties;
    private List<PropertyContainer> conditions;
    private PropertyContainerSet actionSet;

    public HobsonTask() {}

    public HobsonTask(TaskContext context, String name, String description, Map<String, Object> properties, List<PropertyContainer> conditions, PropertyContainerSet actionSet) {
        this.context = context;
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.conditions = conditions;
        this.actionSet = actionSet;
    }

    /**
     * Returns the context of the task.
     *
     * @return a TaskContext instance
     */
    public TaskContext getContext() {
        return context;
    }

    /**
     * Sets the context of the task.
     *
     * @param context a TaskContext intsance
     */
    public void setContext(TaskContext context) {
        this.context = context;
    }

    /**
     * Returns the task name.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the task name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a human-readable description of the task.
     *
     * @return a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a human-readable description of the task.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether this task has any properties associated with it.
     *
     * @return a boolean
     */
    public boolean hasProperties() {
        return (properties != null && properties.size() > 0);
    }

    /**
     * A list of arbitrary properties associated with the task. For example, a scheduler might use one of these
     * properties to indicate a task is scheduled for execution.
     *
     * @return a Properties object
     */
    public Map<String,Object> getProperties() {
        return properties;
    }

    /**
     * Sets a task property.
     *
     * @param name the property name
     * @param value the property value
     */
    public void setProperty(String name, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(name, value);
    }

    /**
     * Sets the task properties.
     *
     * @param properties the full set of task properties
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Returns the condition set for this task.
     *
     * @return a condition set (or null if one hasn't been set)
     */
    public List<PropertyContainer> getConditions() {
        return conditions;
    }

    /**
     * Returns whether this task has any conditions.
     *
     * @return a boolean
     */
    public boolean hasConditions() {
        return (conditions != null && conditions.size() > 0);
    }

    /**
     * Sets the condition set for this task.
     *
     * @param conditions the task conditions
     */
    public void setConditions(List<PropertyContainer> conditions) {
        this.conditions = conditions;
    }

    /**
     * Returns the action set ID associated with this task.
     *
     * @return the action set ID (or null if none is set)
     */
    public PropertyContainerSet getActionSet() {
        return actionSet;
    }

    /**
     * Sets the action set for this task.
     *
     * @param actionSet the new action set
     */
    public void setActionSet(PropertyContainerSet actionSet) {
        this.actionSet = actionSet;
    }

    /**
     * Returns the PropertyContainerClassContexts that this task is dependent on.
     *
     * @param actionClassProvider a provider to resolve action classes
     *
     * @return a Collection of PropertyContainerClassContext instances
     */
    public Collection<PropertyContainerClassContext> getDependencies(TaskActionClassProvider actionClassProvider) {
        List<PropertyContainerClassContext> deps = new ArrayList<>();
        if (conditions != null) {
            for (PropertyContainer pcc : conditions) {
                deps.add(pcc.getContainerClassContext());
            }
        }
        if (actionSet != null && actionSet.hasId() && actionClassProvider != null) {
            Collection<PropertyContainerClassContext> tacs = actionClassProvider.getActionSetClassContexts(actionSet.getId());
            if (tacs != null) {
                for (PropertyContainerClassContext pccc : tacs) {
                    deps.add(pccc);
                }
            }
        }
        return deps;
    }
}
