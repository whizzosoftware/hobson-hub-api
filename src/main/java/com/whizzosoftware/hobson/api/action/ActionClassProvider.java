/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.Collection;

/**
 * An interface that provides action clases.
 *
 * @author Dan Noguerol
 */
public interface ActionClassProvider {
    /**
     * Returns an action class.
     *
     * @param ctx the context
     *
     * @return an ActionClass instance
     */
    ActionClass getActionClass(PropertyContainerClassContext ctx);

    /**
     * Returns the contexts that comprise an action set.
     *
     * @param actionSetId the action set ID
     *
     * @return a Collection of PropertyContainerClassContext instances
     */
    Collection<PropertyContainerClassContext> getActionSetClassContexts(String actionSetId);
}
