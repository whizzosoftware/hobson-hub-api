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
import com.whizzosoftware.hobson.api.variable.VariableContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContextPathIdProviderTest {
    @Test
    public void testCreateUserHubsId() {
        ContextPathIdProvider cdipd = new ContextPathIdProvider();
        assertEquals("users:foo:hubs", cdipd.createUserHubsId("foo"));
    }

    @Test
    public void testCreateHubId() {
        ContextPathIdProvider cdipd = new ContextPathIdProvider();
        assertEquals("users:0891df24-3936-4254-a55f-a4b6e7556fa9:hubs:49a37a38-7e05-4991-a178-ea29a4d9da3e", cdipd.createHubId(HubContext.create("0891df24-3936-4254-a55f-a4b6e7556fa9", "49a37a38-7e05-4991-a178-ea29a4d9da3e")));
    }

    @Test
    public void testCreatePluginId() {
        ContextPathIdProvider cdipd = new ContextPathIdProvider();
        assertEquals("users:foo:hubs:bar:plugins:plugin1", cdipd.createPluginId(PluginContext.create(HubContext.create("foo", "bar"), "plugin1")));
    }

    @Test
    public void testCreatePluginContext() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();
        PluginContext ctx = PluginContext.create(HubContext.create("user1", "hub1"), "plugin1");

        String pluginId = cpidp.createPluginId(ctx);
        assertEquals("users:user1:hubs:hub1:plugins:plugin1", pluginId);

        PluginContext ctx2 = cpidp.createPluginContext(pluginId);
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
    }

    @Test
    public void testCreateDeviceId() {
        ContextPathIdProvider cdipd = new ContextPathIdProvider();
        assertEquals("users:foo:hubs:bar:devices:plugin1:device1", cdipd.createDeviceId(DeviceContext.create(PluginContext.create(HubContext.create("foo", "bar"), "plugin1"), "device1")));
    }

    @Test
    public void testCreateDeviceContext() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();
        DeviceContext ctx2 = cpidp.createDeviceContext("users:user1:hubs:hub1:devices:plugin1:device1");
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
        assertEquals("device1", ctx2.getDeviceId());
    }

    @Test
    public void testCreateDeviceContextWithHub() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();

        DeviceContext ctx2 = cpidp.createDeviceContextWithHub(HubContext.create("user1", "hub1"), "users:user1:hubs:hub1:devices:plugin1:device1");
        assertEquals("user1", ctx2.getUserId());
        assertEquals("hub1", ctx2.getHubId());
        assertEquals("plugin1", ctx2.getPluginId());
        assertEquals("device1", ctx2.getDeviceId());
    }

    @Test
    public void testCreateVariableId() {
        ContextPathIdProvider cdipd = new ContextPathIdProvider();
        assertEquals("users:foo:hubs:bar:variables:plugin1:device1:var1", cdipd.createVariableId(VariableContext.create(DeviceContext.create(PluginContext.create(HubContext.create("foo", "bar"), "plugin1"), "device1"), "var1")));
    }

    @Test
    public void testCreateVariableContext() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();

        VariableContext ctx = cpidp.createVariableContext("users:user1:hubs:hub1:variables:plugin1:device1:videoStatusUrl");
        assertEquals("user1", ctx.getUserId());
        assertEquals("hub1", ctx.getHubId());
        assertEquals("plugin1", ctx.getPluginId());
        assertEquals("device1", ctx.getDeviceId());
        assertEquals("videoStatusUrl", ctx.getName());
    }

    @Test
    public void testCreateDeviceVariablesId() {
        ContextPathIdProvider cpidp = new ContextPathIdProvider();
        assertEquals("users:local:hubs:local:variables:plugin1:device1", cpidp.createDeviceVariablesId(DeviceContext.createLocal("plugin1", "device1")));
    }
}
