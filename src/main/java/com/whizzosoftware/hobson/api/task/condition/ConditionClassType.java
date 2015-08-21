/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.condition;

/**
 * An enumeration for types of condition classes.
 *
 * @author Dan Noguerol
 */
public enum ConditionClassType {
    /**
     * Indicates a trigger condition -- one associated with a specific plugin that knows how to detect when such
     * a condition occurs. For example, the scheduler plugin knows how to detect when a scheduled task trigger occurs
     * based on its condition (e.g. every Wednesday at 1pm).
     */
    trigger,
    /**
     * Indicates an evaluator condition -- one that is evaluated if and only if a task's trigger condition has occurred.
     * For example, a trigger condition might indicate "if device A turns on" and an evaluator condition might make
     * that more specific such as "only if device B is already on". If a task has passed all its evaluator conditions
     * (or doesn't have any) then the task can be executed.
     */
    evaluator
}
