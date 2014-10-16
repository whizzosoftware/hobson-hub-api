/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.manager;

import com.whizzosoftware.hobson.bootstrap.api.HobsonNotFoundException;

public class VariableNotFoundException extends HobsonNotFoundException {
    public VariableNotFoundException(String pluginId, String deviceId, String name) {
        super("Variable " + pluginId + "." + deviceId + "[" + name + "]" + " not found");
    }
}
