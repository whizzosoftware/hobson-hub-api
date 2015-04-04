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
     * Indicates whether a task provider has been published.
     *
     * @param ctx the context of the plugin that published the provider
     * @param providerId the task provider ID
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasTaskProvider(PluginContext ctx, String providerId);

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
     * Publishes a new TaskProvider instance to the runtime.
     *
     * @param provider the task provider to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishTaskProvider(TaskProvider provider);

    /**
     * Unpublishes all task providers published by a plugin.
     *
     * @param ctx the context of the target plugin
     */
    public void unpublishAllTaskProviders(PluginContext ctx);

    /**
     * Adds a new task. This should be called by task providers.
     *
     * @param ctx the context of the target hub
     * @param providerId the task provider ID adding the task
     * @param task the task data
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addTask(HubContext ctx, String providerId, Object task);

    /**
     * Updates an existing task. This should be called by task providers.
     *
     * @param ctx the context of the target hub
     * @param providerId the task provider ID adding the task
     * @param taskId the ID of the task being updated
     * @param task the task data
     */
    public void updateTask(HubContext ctx, String providerId, String taskId, Object task);

    /**
     * Deletes a previously added task.
     *
     * @param ctx the context of the task
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTask(TaskContext ctx);
}
