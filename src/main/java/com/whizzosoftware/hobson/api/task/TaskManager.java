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

import java.util.Collection;
import java.util.List;

/**
 * An interface for managing Hobson tasks.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface TaskManager {
    /**
     * Creates a new task in the system. This is called (e.g. by the REST API) when a request is received to add a new task.
     * @param ctx the hub context
     * @param name the task name
     * @param description the task description
     * @param conditionSet the task's condition set -- its primary condition will determine which plugin is used to create the task
     * @param actionSet the task's action set
     */
    public void createTask(HubContext ctx, String name, String description, PropertyContainerSet conditionSet, PropertyContainerSet actionSet);

    /**
     * Deletes a previously added task.
     *
     * @param ctx the context of the task
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTask(TaskContext ctx);

    /**
     * Executes an action set.
     *
     * @param ctx the context of the action set
     * @param actionSetId the action set ID
     *
     * @since hobson-hub-api 0.5.0
     */
    public void executeActionSet(HubContext ctx, String actionSetId);

    /**
     * Immediately executes a specific task.
     *
     * @param ctx the context of the target hub
     */
    public void executeTask(TaskContext ctx);

    /**
     * Returns an action class.
     *
     * @param ctx the context of the action class to return
     *
     * @return a TaskActionClass instance (or null if not found)
     */
    public PropertyContainerClass getActionClass(PropertyContainerClassContext ctx);

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
    public PropertyContainerSet getActionSet(HubContext ctx, String actionSetId);

    /**
     * Returns all published action classes.
     *
     * @param ctx the context of the hub that published the action classes
     *
     * @return a Collection of HobsonActionClass instances
     *
     * @since hobson-hub-api 0.5.0
     */
    public Collection<PropertyContainerClass> getAllActionClasses(HubContext ctx);

    /**
     * Returns all published action sets.
     *
     * @param ctx the context of the hub that published the action sets
     *
     * @return a Collection of TaskActionSet instances
     */
    public Collection<PropertyContainerSet> getAllActionSets(HubContext ctx);

    /**
     * Returns all published condition classes.
     *
     * @param ctx the context of the hub that published the condition classes
     *
     * @return a Collection of TaskConditionClass instances
     */
    public Collection<PropertyContainerClass> getAllConditionClasses(HubContext ctx);

    /**
     * Returns all tasks that have been published by all task providers.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of HobsonTask objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonTask> getAllTasks(HubContext ctx);

    /**
     * Returns a condition class.
     *
     * @param ctx the context of the condition class
     *
     * @return a TaskConditionClass object (or null if not found)
     */
    public PropertyContainerClass getConditionClass(PropertyContainerClassContext ctx);

    /**
     * Returns a specific task.
     *
     * @param ctx the context of the task
     *
     * @return a HobsonTask instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonTask getTask(TaskContext ctx);

    /**
     * Publish an action class.
     *
     * @param context the action class context
     * @param name the action class name
     * @param properties the list of supported properties
     */
    public void publishActionClass(PropertyContainerClassContext context, String name, List<TypedProperty> properties);

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
    public PropertyContainerSet publishActionSet(HubContext ctx, String name, List<PropertyContainer> actions);

    /**
     * Publishes a condition class.
     *
     * @param ctx the condition class context
     * @param name the name of the condition class
     * @param properties a list of supported properties
     */
    public void publishConditionClass(PropertyContainerClassContext ctx, String name, List<TypedProperty> properties);

    /**
     * Publishes a task. This is called by plugins to fulfill requests to add new tasks to the system.
     *
     * @param task the task to publish
     */
    public void publishTask(HobsonTask task);

    /**
     * Unpublish all action classes published by a plugin.
     *
     * @param ctx the plugin context
     */
    public void unpublishAllActionClasses(PluginContext ctx);

    /**
     * Unpublishes all action sets previously published by a plugin.
     *
     * @param ctx the context of the plugin that published the action sets
     */
    public void unpublishAllActionSets(PluginContext ctx);

    /**
     * Unpublish all condition classes published by a plugin.
     *
     * @param ctx the plugin context
     */
    public void unpublishAllConditionClasses(PluginContext ctx);

    /**
     * Unpublish all tasks published by a plugin.
     *
     * @param ctx the plugin context
     */
    public void unpublishAllTasks(PluginContext ctx);

    /**
     * Unpublish a task.
     *
     * @param ctx the task context
     */
    public void unpublishTask(TaskContext ctx);

    /**
     * Updates an existing task.
     *
     * @param ctx the context of the task to update
     * @param name the new task name
     * @param description the description of the new task
     * @param conditionSet the task's condition set
     * @param actionSet  the task's action set
     */
    public void updateTask(TaskContext ctx, String name, String description, PropertyContainerSet conditionSet, PropertyContainerSet actionSet);

    /**
     * Provides notification that a task has executed.
     *
     * @param task the task that executed
     */
    public void fireTaskExecutionEvent(HobsonTask task, long time, Throwable error);
}
