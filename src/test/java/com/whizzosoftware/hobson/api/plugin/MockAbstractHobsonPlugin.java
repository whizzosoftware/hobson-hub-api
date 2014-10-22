/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import java.util.Dictionary;

public class MockAbstractHobsonPlugin extends AbstractHobsonPlugin {
    private String name;

    public MockAbstractHobsonPlugin(String pluginId, String name) {
        super(pluginId);
        this.name = name;
    }

    @Override
    public void onStartup(Dictionary config) {

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
    public void onPluginConfigurationUpdate(Dictionary config) {

    }

    @Override
    public String getName() {
        return name;
    }
}
