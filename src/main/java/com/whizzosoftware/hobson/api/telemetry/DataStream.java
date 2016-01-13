/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;

/**
 * A named collection of variables that define a telemetry data stream.
 *
 * @author Dan Noguerol
 */
public class DataStream {
    private String name;
    private Collection<VariableContext> variables;

    public DataStream(String name, Collection<VariableContext> variables) {
        this.name = name;
        this.variables = variables;
    }

    public String getName() {
        return name;
    }

    public Collection<VariableContext> getVariables() {
        return variables;
    }
}
