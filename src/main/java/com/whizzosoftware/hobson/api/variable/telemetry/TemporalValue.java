/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.telemetry;

/**
 * A value that has a time associated with it.
 *
 * @author Dan Noguerol
 */
public class TemporalValue {
    private long time;
    private Object value;

    public TemporalValue(long time, Object value) {
        this.time = time;
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public Object getValue() {
        return value;
    }
}
