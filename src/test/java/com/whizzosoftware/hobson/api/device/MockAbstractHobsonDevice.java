/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;

import java.util.HashMap;
import java.util.Map;

public class MockAbstractHobsonDevice extends AbstractHobsonDevice {
    public final Map<String,Object> setVariableRequests = new HashMap<String,Object>();
    public boolean wasStartupCalled;
    public boolean wasShutdownCalled;

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     * @param id     the device ID
     */
    public MockAbstractHobsonDevice(HobsonPlugin plugin, String id) {
        super(plugin, id);
    }

    @Override
    public TypedProperty[] createConfigurationPropertyMetaData() {
        return new TypedProperty[0];
    }

    @Override
    public void onStartup(PropertyContainer config) {
        super.onStartup(config);
        wasStartupCalled = true;
    }

    @Override
    public void onShutdown() {
        wasShutdownCalled = true;
    }

    @Override
    public DeviceType getType() {
        return null;
    }

    @Override
    public DeviceError getError() {
        return null;
    }

    @Override
    public void onSetVariable(String variableName, Object value) {
        setVariableRequests.put(variableName, value);
    }
}
