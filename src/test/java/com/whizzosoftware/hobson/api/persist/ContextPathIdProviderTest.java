/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContextPathIdProviderTest {
    @Test
    public void testPluginContext() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();
        PluginContext ctx = PluginContext.create(HubContext.create("user1", "hub1"), "plugin1");

        String pluginId = cpidp.createPluginId(ctx);
        assertEquals("user1:hubs:hub1:plugins:plugin1", pluginId);

        PluginContext ctx2 = cpidp.createPluginContext(pluginId);
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
    }

    @Test
    public void testDeviceContext() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();
        DeviceContext ctx = DeviceContext.create(HubContext.create("user1", "hub1"), "plugin1", "device1");

        String deviceId = cpidp.createDeviceId(ctx);
        assertEquals("user1:hubs:hub1:devices:plugin1:device1", deviceId);

        DeviceContext ctx2 = cpidp.createDeviceContext(deviceId);
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
        assertEquals("device1", ctx2.getDeviceId());
    }

    @Test
    public void testDeviceContextWithHub() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();

        DeviceContext ctx2 = cpidp.createDeviceContextWithHub(HubContext.create("user1", "hub1"), "local:hubs:local:devices:plugin1:device1");
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
        assertEquals("device1", ctx2.getDeviceId());
    }
}
