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

/**
 * Interface that is passed to action lifecycle methods to give them the opportunity to alter the action
 * lifecycle.
 *
 * @author Dan Noguerol
 */
public interface ActionLifecycleContext {
    void complete();
    void complete(String msg);
    void fail(String msg);
    void update(String msg);
}
