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

import java.util.Collection;

/**
 * An interface for managing Hobson tasks.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface TaskManager {
    /**
     * Adds a new task to the system. This is called (e.g. by the REST API) when a request is received to add a new
     * task to the system.
     *
     * @param ctx the context of the plugin
     * @param config the task configuration data
     */
    public void addTask(PluginContext ctx, Object config);

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
     * Immediately executes a specific task.
     *
     * @param ctx the context of the target hub
     */
    public void executeTask(TaskContext ctx);

    /**
     * Publishes a task. This is called by plugins to fulfill requests to add new tasks to the system.
     *
     * @param task the task to publish
     */
    public void publishTask(HobsonTask task);

    /**
     * Unpublish all tasks published by a plugin.
     *
     * @param ctx the plugin context
     */
    public void unpublishAllTasks(PluginContext ctx);

    /**
     * Updates an existing task.
     *
     * @param ctx the context of the task to update
     * @param config the configuration data to update the task with
     */
    public void updateTask(TaskContext ctx, Object config);

    /**
     * Deletes a previously added task.
     *
     * @param ctx the context of the task
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTask(TaskContext ctx);
}
