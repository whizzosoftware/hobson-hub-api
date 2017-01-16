/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event;

import java.lang.reflect.Method;

/**
 * An interface used to invoke event callback methods.
 *
 * @author Dan Noguerol
 */
public interface EventCallbackInvoker {
    void invoke(Method m, Object o, HobsonEvent e);
}
