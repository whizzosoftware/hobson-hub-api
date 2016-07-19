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
    private String preferredVariableName;
    private boolean started;

    private HobsonDeviceStub(DeviceContext ctx) {
        this.ctx = ctx;
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
        return started;
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
    public HobsonDeviceRuntime getRuntime() {
        return null;
    }

    static public class Builder {
        private HobsonDeviceStub stub;

        public Builder(DeviceContext dctx) {
            stub = new HobsonDeviceStub(dctx);
        }

        public Builder(HobsonDevice d) {
            stub = new HobsonDeviceStub(d.getContext());
            stub.name = d.getName();
            stub.type = d.getType();
            stub.manufacturerName = d.getManufacturerName();
            stub.manufacturerVersion = d.getManufacturerVersion();
            stub.modelName = d.getModelName();
            stub.preferredVariableName = d.getPreferredVariableName();
        }

        public Builder name(String name) {
            stub.name = name;
            return this;
        }

        public Builder type(DeviceType type) {
            stub.type = type;
            return this;
        }

        public Builder manufacturerName(String manufacturerName) {
            stub.manufacturerName = manufacturerName;
            return this;
        }

        public Builder modelName(String modelName) {
            stub.modelName = modelName;
            return this;
        }

        public Builder manufacturerVersion(String manufacturerVersion) {
            stub.manufacturerVersion = manufacturerVersion;
            return this;
        }

        public Builder preferredVariableName(String preferredVariableName) {
            stub.preferredVariableName = preferredVariableName;
            return this;
        }

        public Builder started(boolean started) {
            stub.started = started;
            return this;
        }

        public HobsonDeviceStub build() {
            return stub;
        }
    }
}
