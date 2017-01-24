/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceError;
import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;

import java.util.Map;

/**
 * An interface for classes that act as a proxy between Hobson and actual hardware.
 *
 * @author Dan Noguerol
 */
public interface HobsonDeviceProxy {
    PropertyContainerClass getConfigurationClass();
    DeviceContext getContext();
    String getDefaultName();
    HobsonDeviceDescriptor getDescriptor();
    DeviceError getError();
    Long getLastCheckin();
    String getManufacturerName();
    String getManufacturerVersion();
    String getModelName();
    String getName();
    String getPreferredVariableName();
    DeviceType getType();
    DeviceVariableState getVariableState(String name);
    boolean hasError();
    boolean hasVariable(String name);
    boolean isStarted();
    void onDeviceConfigurationUpdate(Map<String,Object> config);
    void onSetVariables(Map<String,Object> values);
    void onShutdown();
    void onStartup(String name, Map<String,Object> config);
    void setLastCheckin(Long lastCheckin);
    void start(String name, Map<String,Object> config);
}
