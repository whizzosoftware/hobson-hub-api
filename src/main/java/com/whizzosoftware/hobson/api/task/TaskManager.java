/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
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
     * Executes an action set.
     *
     * @param ctx the context of the action set
     * @param actionSetId the action set ID
     *
     * @since hobson-hub-api 0.5.0
     */
    void executeActionSet(HubContext ctx, String actionSetId);

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
     * Returns an action class.
     *
     * @param ctx the context of the action class to return
     *
     * @return a TaskActionClass instance (or null if not found)
     */
    TaskActionClass getActionClass(PropertyContainerClassContext ctx);

    /**
     * Returns a published action set.
     *
     * @param ctx the context of the action set
     * @param actionSetId the action set ID
     *
     * @return a HobsonActionSet instance (or null if not found)
     *
     * @since hobson-hub-api 0.5.0
     */
    PropertyContainerSet getActionSet(HubContext ctx, String actionSetId);

    /**
     * Returns all published action classes.
     *
     * @param ctx the context of the hub that published the action classes
     * @param applyConstraints only return condition classes for which the constraints of their typed properties can be
     *                         met by the currently available system services (i.e. don't show the user things they
     *                         can't do)
     *
     * @return a Collection of TaskActionClass instances
     *
     * @since hobson-hub-api 0.5.0
     */
    Collection<TaskActionClass> getAllActionClasses(HubContext ctx, boolean applyConstraints);

    /**
     * Returns all published action sets.
     *
     * @param ctx the context of the hub that published the action sets
     *
     * @return a Collection of TaskActionSet instances
     */
    Collection<PropertyContainerSet> getAllActionSets(HubContext ctx);

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
    Collection<TaskConditionClass> getAllConditionClasses(HubContext ctx, ConditionClassType type, boolean applyConstraints);

    /**
     * Returns all tasks that have been published by all task providers.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonTask objects
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonTask> getAllTasks(HubContext ctx);

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
     * Publish an action class.
     *
     * @param actionClass the action class to publish
     */
    void publishActionClass(TaskActionClass actionClass);

    /**
     * Creates and publishes a new action set.
     *
     * @param ctx the context of the hub publishing the action set
     * @param name the name of the action set
     * @param actions the actions to include in the set
     *
     * @return a TaskActionSet instance
     *
     * @since hobson-hub-api 0.5.0
     */
    PropertyContainerSet publishActionSet(HubContext ctx, String name, List<PropertyContainer> actions);

    /**
     * Publishes a condition class.
     *
     * @param conditionClass the condition class to publish
     */
    void publishConditionClass(TaskConditionClass conditionClass);

    /**
     * Unpublish all action classes published by a plugin.
     *
     * @param ctx the plugin context
     */
    void unpublishAllActionClasses(PluginContext ctx);

    /**
     * Unpublishes all action sets previously published by a plugin.
     *
     * @param ctx the context of the plugin that published the action sets
     */
    void unpublishAllActionSets(PluginContext ctx);

    /**
     * Unpublish all condition classes published by a plugin.
     *
     * @param ctx the plugin context
     */
    void unpublishAllConditionClasses(PluginContext ctx);

    /**
     * Updates an existing task.
     * @param ctx the context of the task to update
     * @param name the new task name
     * @param description the description of the new task
     * @param conditions the task's conditions
     * @param actionSet  the task's action set
     */
    void updateTask(TaskContext ctx, String name, String description, List<PropertyContainer> conditions, PropertyContainerSet actionSet);

    void updateTaskProperties(TaskContext ctx, Map<String,Object> properties);
}
