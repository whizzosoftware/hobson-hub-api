/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

/**
 * A stub implementation of a Hobson device that provides meta-data but does not allow for control. This is used
 * primarily for passing around de-serialized device information.
 *
 * @author Dan Noguerol
 */
public class HobsonDeviceStub implements HobsonDevice {
    private DeviceContext ctx;
    private String name;
    private DeviceType type;
    private String manufacturerName;
    private String modelName;
    private String manufacturerVersion;
    private Long lastCheckIn;
    private String preferredVariableName;

    public HobsonDeviceStub(DeviceContext ctx, String name, DeviceType type, Long lastCheckIn, String preferredVariableName) {
        this.ctx = ctx;
        this.name = name;
        this.type = type;
        this.lastCheckIn = lastCheckIn;
        this.preferredVariableName = preferredVariableName;
    }

    public HobsonDeviceStub(HobsonDevice d) {
        this.ctx = d.getContext();
        this.name = d.getName();
        this.type = d.getType();
        this.manufacturerName = d.getManufacturerName();
        this.modelName = d.getModelName();
        this.manufacturerVersion = d.getManufacturerVersion();
        this.lastCheckIn = d.getLastCheckIn();
        this.preferredVariableName = d.getPreferredVariableName();
    }

    @Override
    public DeviceContext getContext() {
        return ctx;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public String getManufacturerName() {
        return manufacturerName;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public String getManufacturerVersion() {
        return manufacturerVersion;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public DeviceError getError() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public Long getLastCheckIn() {
        return lastCheckIn;
    }

    @Override
    public boolean hasPreferredVariableName() {
        return (preferredVariableName != null);
    }

    @Override
    public String getPreferredVariableName() {
        return preferredVariableName;
    }

    @Override
    public PropertyContainerClass getConfigurationClass() {
        return null;
    }

    @Override
    public boolean isTelemetryCapable() {
        return false;
    }

    @Override
    public String[] getTelemetryVariableNames() {
        return null;
    }

    @Override
    public HobsonDeviceRuntime getRuntime() {
        return null;
    }
}
