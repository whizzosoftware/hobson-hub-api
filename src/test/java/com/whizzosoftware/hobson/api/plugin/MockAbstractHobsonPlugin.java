/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.property.PropertyContainer;

public class MockAbstractHobsonPlugin extends AbstractHobsonPlugin {
    private String name;

    public MockAbstractHobsonPlugin(String pluginId, String name) {
        super(pluginId, new MockEventLoopGroup());
        this.name = name;
    }

    @Override
    public void onStartup(PropertyContainer config) {

    }

    @Override
    public void onShutdown() {

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

    @Override
    public String getName() {
        return name;
    }
}
