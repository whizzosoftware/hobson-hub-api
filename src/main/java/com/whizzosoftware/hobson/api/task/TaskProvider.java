/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.action.ActionManager;

import java.util.Collection;

/**
 * Interface for classes that can contribute tasks to the system.
 *
 * @author Dan Noguerol
 */
public interface TaskProvider {
    /**
     * Returns the ID of the plugin which created this task provider.
     *
     * @return a plugin ID
     */
    public String getPluginId();

    /**
     * Returns the ID of this task provider.
     *
     * @return the task provider ID
     */
    public String getId();

    /**
     * Sets the ActionManager instance this provider should use to execute actions.
     *
     * @param actionManager an ActionManager instance
     */
    public void setActionManager(ActionManager actionManager);

    /**
     * Returns all tasks for this provider.
     *
     * @return a Collection of HobsonTask instances
     */
    public Collection<HobsonTask> getTasks();

    /**
     * Returns a specific task.
     *
     * @param taskId the task ID
     *
     * @return a HobsonTask instance (or null if not found)
     */
    public HobsonTask getTask(String taskId);

    /**
     * Adds a new task.
     *
     * @param task the task
     */
    public void addTask(Object task);

    /**
     * Updates an existing task.
     *
     * @param taskId the ID of the task
     * @param data the task data
     */
    public void updateTask(String taskId, Object data);

    /**
     * Removes an existing task.
     *
     * @param taskId the ID of the task
     */
    public void deleteTask(String taskId);
}
