/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.action;

import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.Collection;

/**
 * An interface that provides access to action class information.
 *
 * @author Dan Noguerol
 */
public interface TaskActionClassProvider {
    /**
     * Returns the action class for a context.
     *
     * @param ctx the context
     *
     * @return a TaskActionClass instance (or null if not found)
     */
    public TaskActionClass getActionClass(PropertyContainerClassContext ctx);

    /**
     * Returns the list of action contexts associated with an action set.
     *
     * @param actionSetId the ID of the action set
     *
     * @return a Collection of PropertyContainerClassContext objects
     */
    public Collection<PropertyContainerClassContext> getActionSetClassContexts(String actionSetId);
}
