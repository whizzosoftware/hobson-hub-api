/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.event.HobsonEvent;

import java.util.Dictionary;

/**
 * An interface for classes that want to receive callbacks from an event loop.
 *
 * @author Dan Noguerol
 */
public interface PluginEventLoopListener {
    public void onEventLoopInitializing(Dictionary config);
    public void onEventLoopRefresh();
    public void onEventLoopStop();
    public void onEventLoopPluginConfigChange(Dictionary config);
    public void onEventLoopDeviceConfigChange(String deviceId, Dictionary config);
    public void onEventLoopEvent(HobsonEvent event);
}
