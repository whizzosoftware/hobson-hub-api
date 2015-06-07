/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.property.PropertyContainerSet;

/**
 * Interface for plugins that can contribute tasks to the system.
 *
 * @author Dan Noguerol
 */
public interface TaskProvider {
    /**
     * Callback for a new task creation request.
     *  @param name the name of the new task
     * @param description
     * @param conditionSet conditions that must evaluate to true for the task to execute
     * @param actionSet the action set to execute when the task runs
     */
    public void onCreateTask(String name, String description, PropertyContainerSet conditionSet, PropertyContainerSet actionSet);

    /**
     * Callback for a task update request.
     *  @param ctx the context of the task to update
     * @param name the name of the new task
     * @param description
     * @param conditionSet conditions that must evaluate to true for the task to execute
     * @param actionSet the action set to execute when the task runs
     */
    public void onUpdateTask(TaskContext ctx, String name, String description, PropertyContainerSet conditionSet, PropertyContainerSet actionSet);

    /**
     * Callback for a task deletion request.
     *
     * @param ctx the context of the task to remove
     */
    public void onDeleteTask(TaskContext ctx);
}
