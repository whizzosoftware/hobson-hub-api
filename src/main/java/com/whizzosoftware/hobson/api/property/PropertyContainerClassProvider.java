/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

/**
 * An interface that can perform property container class lookup.
 *
 * @author Dan Noguerol
 */
public interface PropertyContainerClassProvider {
    PropertyContainerClass getPropertyContainerClass(PropertyContainerClassContext ctx);
}
