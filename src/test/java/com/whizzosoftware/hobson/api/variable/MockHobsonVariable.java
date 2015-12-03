/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

public class MockHobsonVariable implements HobsonVariable {
    private String pluginId;
    private String deviceId;
    private String name;
    private Object value;
    private Mask mask;
    private Long lastUpdate;
    private VariableProxyType proxyType;

    public MockHobsonVariable(String pluginId, String deviceId, String name, Object value, Mask mask) {
        this(pluginId, deviceId, name, value, mask, null);
    }

    public MockHobsonVariable(String pluginId, String deviceId, String name, Object value, Mask mask, VariableProxyType proxyType) {
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
        this.value = value;
        this.mask = mask;
        this.proxyType = proxyType;
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public boolean hasProxyType() {
        return (getProxyType() != null);
    }

    @Override
    public VariableProxyType getProxyType() {
        return proxyType;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
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

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean isGlobal() {
        return (deviceId == null);
    }
}
