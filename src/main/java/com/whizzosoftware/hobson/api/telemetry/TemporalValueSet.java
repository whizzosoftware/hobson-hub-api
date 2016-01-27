/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of values that have a time associated with them.
 *
 * @author Dan Noguerol
 */
public class TemporalValueSet {
    private long time;
    private Map<String,Object> values;

    public TemporalValueSet(long time) {
        this(time, new HashMap<String,Object>());
    }

    public TemporalValueSet(long time, Map<String,Object> values) {
        this.time = time;
        this.values = values;
    }

    public void addValue(String name, Object value) {
        values.put(name, value);
    }

    public long getTime() {
        return time;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
