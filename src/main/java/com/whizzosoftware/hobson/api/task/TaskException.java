/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

/**
 * Generic task exception.
 *
 * @author Dan Noguerol
 */
public class TaskException extends RuntimeException {
    public TaskException(String msg) {
        super(msg);
    }

    public TaskException(String msg, Throwable t) {
        super(msg, t);
    }
}
