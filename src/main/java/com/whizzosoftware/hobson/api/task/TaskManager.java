/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import java.util.Collection;

/**
 * An interface for managing Hobson tasks.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface TaskManager {
    /**
     * Publishes a new TaskProvider instance to the runtime.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param provider the task provider to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishTaskProvider(String userId, String hubId, TaskProvider provider);

    /**
     * Indicates whether a task provider has been published.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the task provider
     * @param providerId the task provider ID
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasTaskProvider(String userId, String hubId, String pluginId, String providerId);

    /**
     * Returns all tasks that have been published by all task providers.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Collection of HobsonTask objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonTask> getAllTasks(String userId, String hubId);

    /**
     * Returns a specific task.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the provider ID
     * @param taskId the task ID
     *
     * @return a HobsonTask instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonTask getTask(String userId, String hubId, String providerId, String taskId);

    /**
     * Immediately executes a specific task.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the task provider ID
     * @param taskId the task ID
     */
    public void executeTask(String userId, String hubId, String providerId, String taskId);

    /**
     * Adds a new task. This should be called by task providers.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the task provider ID adding the task
     * @param task the task to add
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addTask(String userId, String hubId, String providerId, Object task);

    /**
     * Deletes a previously added task.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the task provider ID that added the task
     * @param taskId the task ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void deleteTask(String userId, String hubId, String providerId, String taskId);
}