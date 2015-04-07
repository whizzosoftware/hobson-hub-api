/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

/**
 * Interface for plugins that can contribute tasks to the system.
 *
 * @author Dan Noguerol
 */
public interface TaskProvider {
    /**
     * Callback for a new task creation request.
     *
     * @param config the configuration for the new task
     */
    public void onCreateTask(Object config);

    /**
     * Callback for a task update request.
     *
     * @param task the task being updated
     * @param config the new configuration for the task
     */
    public void onUpdateTask(HobsonTask task, Object config);

    /**
     * Callback for a task deletion request.
     *
     * @param task the task to remove
     */
    public void onDeleteTask(HobsonTask task);
}
