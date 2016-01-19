/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * Concrete implementation of HobsonVariable.
 *
 * @author Dan Noguerol
 */
public class MutableHobsonVariable extends ImmutableHobsonVariable {
    public MutableHobsonVariable(VariableContext ctx, Mask mask, Object value) {
        this(ctx, mask, value, null, null);
    }

    public MutableHobsonVariable(VariableContext ctx, Mask mask, Object value, Long lastUpdate) {
        this(ctx, mask, value, lastUpdate, null);
    }

    public MutableHobsonVariable(VariableContext ctx, Mask mask, Object value, Long lastUpdate, VariableMediaType mediaType) {
        super(ctx, mask, value, lastUpdate, mediaType);
    }

    public void setValue(Object value) {
        this.value = value;
        setLastUpdate(System.currentTimeMillis());
    }

    public void setLastUpdate(Long time) {
        this.lastUpdate = time;
    }
}
