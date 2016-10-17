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

public interface ActionClassProvider {
    ActionClass getActionClass(PropertyContainerClassContext ctx);
    Collection<PropertyContainerClassContext> getActionSetClassContexts(String actionSetId);
}
