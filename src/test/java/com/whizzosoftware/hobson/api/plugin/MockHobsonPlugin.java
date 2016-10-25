/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import io.netty.util.concurrent.Future;

public class MockHobsonPlugin extends AbstractHobsonPlugin {
    private String name;

    public MockHobsonPlugin(String pluginId, String name, String version, String description) {
        super(pluginId, version, description, new MockEventLoopGroup());
        this.name = name;
    }

    public Future publishDeviceProxy(HobsonDeviceProxy proxy) {
        return super.publishDeviceProxy(proxy);
    }


    @Override
    public void onStartup(PropertyContainer config) {

    }

    @Override
    public void onShutdown() {

    }

    @Override
    protected TypedProperty[] getConfigurationPropertyTypes() {
        return null;
    }

    @Override
    protected String getName() {
        return name;
    }

    @Override
    public long getRefreshInterval() {
        return 0;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPluginConfigurationUpdate(PropertyContainer config) {

    }
}
