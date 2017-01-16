/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.io.Serializable;

/**
 * An enumeration for the different PropertyContainerClass types.
 *
 * @author Dan Noguerol
 */
public enum PropertyContainerClassType implements Serializable { // TODO: remove
    ACTION,
    CONDITION,
    HUB_CONFIG,
    PLUGIN_CONFIG,
    DEVICE_CONFIG
}
