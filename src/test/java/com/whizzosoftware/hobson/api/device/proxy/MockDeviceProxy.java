/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.DeviceProxyVariable;

import java.util.HashMap;
import java.util.Map;

public class MockDeviceProxy extends AbstractDeviceProxy {
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
    public void onDeviceConfigurationUpdate(PropertyContainer config) {
    }

    @Override
    public TypedProperty[] createConfigurationPropertyTypes() {
        return null;
    }

    @Override
    public void onStartup(String name, PropertyContainer config) {

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
    public void onSetVariable(String variableName, Object value) {
        setVariableRequests.put(variableName, value);
    }
}
