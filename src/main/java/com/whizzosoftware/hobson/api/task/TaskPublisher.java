/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

/**
 * An interface for managing Hobson tasks.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.5.0
 */
public interface TaskPublisher {
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
     * Unpublishes all task providers published by a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     */
    public void unpublishAllTaskProviders(String userId, String hubId, String pluginId);

    /**
     * Adds a new task. This should be called by task providers.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the task provider ID adding the task
     * @param task the task data
     *
     * @since hobson-hub-api 0.1.6
     */
    public void addTask(String userId, String hubId, String providerId, Object task);

    /**
     * Updates an existing task. This should be called by task providers.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param providerId the task provider ID adding the task
     * @param taskId the ID of the task being updated
     * @param task the task data
     */
    public void updateTask(String userId, String hubId, String providerId, String taskId, Object task);

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
