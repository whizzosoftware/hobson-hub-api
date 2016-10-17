package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceError;
import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;

public interface HobsonDeviceProxy {
    ActionClass getActionClass(String actionClassId);
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
    DeviceType getType();
    DeviceVariableState getVariableState(String name);
    boolean hasError();
    boolean isStarted();
    String getPreferredVariableName();
    void onDeviceConfigurationUpdate(PropertyContainer config);
    void onSetVariable(String name, Object value);
    void onShutdown();
    void onStartup(String name, PropertyContainer config);
    void setLastCheckin(Long lastCheckin);
    void start(String name, PropertyContainer config);
}
