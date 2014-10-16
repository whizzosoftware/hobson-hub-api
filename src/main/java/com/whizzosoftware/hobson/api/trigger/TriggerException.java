/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger;

/**
 * Generic trigger exception.
 *
 * @author Dan Noguerol
 */
public class TriggerException extends RuntimeException {
    public TriggerException(String msg) {
        super(msg);
    }

    public TriggerException(String msg, Throwable t) {
        super(msg, t);
    }
}
