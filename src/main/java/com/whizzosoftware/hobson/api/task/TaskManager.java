/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClassProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An interface for managing Hobson tasks.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface TaskManager extends TaskConditionClassProvider {
    /**
     * Creates a new task in the system. This is called (e.g. by the REST API) when a request is received to add a new task.
     *
     * @param ctx the hub context
     * @param name the task name
     * @param description the task description
     * @param conditions the task's conditions
     * @param actionSet the task's action set
     */
    void createTask(HubContext ctx, String name, String description, List<PropertyContainer> conditions, PropertyContainerSet actionSet);

    /**
     * Deletes a previously added task.
     *
     * @param ctx the context of the task
     *
     * @since hobson-hub-api 0.1.6
     */
    void deleteTask(TaskContext ctx);

    /**
     * Manually executes a task.
     *
     * @param ctx the context of the task to execute
     */
    void executeTask(TaskContext ctx);

    /**
     * Indicates that a task trigger condition has been met. This is usually called by plugins that detect
     * trigger conditions and will cause the TaskManager to evaluate any additional evaluator conditions the task
     * may have. If they all evaluate to true, or if no evaluator conditions exist, the task's action set will be
     * executed.
     *
     * @param ctx the context of the target hub
     */
    void fireTaskTrigger(TaskContext ctx);

    /**
     * Returns all published condition classes.
     *
     * @param ctx the context of the hub that published the condition classes
     * @param type filter the results by a type of condition class (or null for all)
     * @param applyConstraints only return condition classes for which the constraints of their typed properties can be
     *                         met by the currently available system services (i.e. don't show the user things they
     *                         can't do)
     *
     * @return a Collection of TaskConditionClass instances
     */
    Collection<TaskConditionClass> getConditionClasses(HubContext ctx, ConditionClassType type, boolean applyConstraints);

    /**
     * Returns a specific task.
     *
     * @param ctx the context of the task
     *
     * @return a HobsonTask instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    HobsonTask getTask(TaskContext ctx);

    /**
     * Returns all tasks that have been published by all task providers.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonTask objects
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonTask> getTasks(HubContext ctx);

    /**
     * Publishes a condition class.
     *
     * @param conditionClass the condition class to publish
     */
    void publishConditionClass(TaskConditionClass conditionClass);

    /**
     * Updates an existing task.
     * @param ctx the context of the task to update
     * @param name the new task name
     * @param description the description of the new task
     * @param conditions the task's conditions
     * @param actionSet  the task's action set
     */
    void updateTask(TaskContext ctx, String name, String description, boolean enabled, List<PropertyContainer> conditions, PropertyContainerSet actionSet);

    /**
     * Updates a task's properties.
     *
     * @param ctx the task context
     * @param properties the new properties
     */
    void updateTaskProperties(TaskContext ctx, Map<String,Object> properties);
}
