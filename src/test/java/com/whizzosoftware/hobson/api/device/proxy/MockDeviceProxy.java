/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;

import java.util.HashMap;
import java.util.Map;

public class MockDeviceProxy extends AbstractHobsonDeviceProxy {
    public final Map<String,Object> setVariableRequests = new HashMap<String,Object>();
    public boolean wasShutdownCalled;

    public MockDeviceProxy(HobsonPlugin plugin, String id, DeviceType deviceType) {
        this(plugin, id, deviceType, id);
    }

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     */
    public MockDeviceProxy(HobsonPlugin plugin, String id, DeviceType deviceType, String defaultName) {
        super(plugin, id, defaultName, deviceType);
    }

    @Override
    public void onShutdown() {
        wasShutdownCalled = true;
    }

    @Override
    public void onDeviceConfigurationUpdate(Map<String,Object> config) {
    }

    @Override
    public TypedProperty[] getConfigurationPropertyTypes() {
        return null;
    }

    @Override
    public void onStartup(String name, Map<String,Object> config) {
    }

    @Override
    public String getManufacturerName() {
        return null;
    }

    @Override
    public String getManufacturerVersion() {
        return null;
    }

    @Override
    public String getModelName() {
        return null;
    }

    @Override
    public String getPreferredVariableName() {
        return null;
    }

    @Override
    public void onSetVariables(Map<String,Object> values) {
        setVariableRequests.putAll(values);
    }
}
