/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * A stub implemention of HobsonVariable.
 *
 * @author Dan Noguerol
 */
public class HobsonVariableStub implements HobsonVariable {
    private String pluginId;
    private String deviceId;
    private String name;
    private Mask mask;
    private Long lastUpdate;
    private Object value;
    private boolean global;

    public HobsonVariableStub(String pluginId, String deviceId, String name, Mask mask, Long lastUpdate, Object value, boolean global) {
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
        this.mask = mask;
        this.lastUpdate = lastUpdate;
        this.value = value;
        this.global = global;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Mask getMask() {
        return mask;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public boolean hasProxyType() {
        return false;
    }

    @Override
    public VariableProxyType getProxyType() {
        return null;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isGlobal() {
        return global;
    }
}
