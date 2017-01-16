/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.data;

import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;

/**
 * Represents a field in a data stream. It is essentially a named reference to a variable.
 *
 * @author Dan Noguerol
 */
public class DataStreamField {
    private String id;
    private String name;
    private DeviceVariableContext ctx;

    public DataStreamField(String id, String name, DeviceVariableContext ctx) {
        this.id = id;
        this.name = name;
        this.ctx = ctx;
    }

    public String getId() {
        return id;
    }

    public boolean hasId() {
        return (id != null);
    }

    public String getName() {
        return name;
    }

    public DeviceVariableContext getVariable() {
        return ctx;
    }
}
