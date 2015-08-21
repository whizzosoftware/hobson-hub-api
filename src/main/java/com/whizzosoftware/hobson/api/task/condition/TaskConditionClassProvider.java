/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.condition;

import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

/**
 * An interface for classes that provide access to condition classes.
 *
 * @author Dan Noguerol
 */
public interface TaskConditionClassProvider {
    /**
     * Returns the condition class for a specific context.
     *
     * @param ctx the context
     *
     * @return a TaskConditionClass instance (or null if not found)
     */
    TaskConditionClass getConditionClass(PropertyContainerClassContext ctx);
}
