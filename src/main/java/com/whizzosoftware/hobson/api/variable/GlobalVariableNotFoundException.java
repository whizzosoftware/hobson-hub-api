/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;

/**
 * An exception that occurs when a global variable cannot be found (e.g. when requesting one).
 *
 * @author Dan Noguerol
 */
public class GlobalVariableNotFoundException extends HobsonNotFoundException {
    public GlobalVariableNotFoundException(String name) {
        super("Global variable [" + name + "]" + " not found");
    }
}
