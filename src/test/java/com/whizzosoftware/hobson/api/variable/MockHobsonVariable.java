/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

public class MockHobsonVariable implements HobsonVariable {
    private String name;
    private Object value;
    private Mask mask;
    private Long lastUpdate;

    public MockHobsonVariable(String name, Object value, Mask mask) {
        this.name = name;
        this.value = value;
        this.mask = mask;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Mask getMask() {
        return mask;
    }

    @Override
    public Long getLastUpdate() {
        return lastUpdate;
    }
}
