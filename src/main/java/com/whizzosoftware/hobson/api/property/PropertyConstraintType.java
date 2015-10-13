/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

/**
 * An enumeration of constraints that can be placed on a typed property value.
 *
 * @author Dan Noguerol
 */
public enum PropertyConstraintType {
    /**
     * Indicates that the property is required.
     */
    required,
    /**
     * Indicates that the property is only valid for a specific device type.
     */
    deviceType,
    /**
     * Indicates that a device must possess a certain variable.
     */
    deviceVariable
}
