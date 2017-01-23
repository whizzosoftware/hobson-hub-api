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

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.MockActionManager;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.device.proxy.MockDeviceProxy;
import com.whizzosoftware.hobson.api.event.MockEventManager;
import com.whizzosoftware.hobson.api.event.plugin.PluginConfigurationUpdateEvent;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class AbstractHobsonPluginTest {
    @Test
    public void testPublishAndStartDeviceWithNoDeviceManager() {
        try {
            MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
            plugin.publishDeviceProxy(new MockDeviceProxy(plugin, "pid", DeviceType.LIGHTBULB));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManager() throws Exception {
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getDevices(HubContext.createLocal()).size());
        Future future = plugin.publishDeviceProxy(new MockDeviceProxy(plugin, "did", DeviceType.LIGHTBULB));
        future.await();
        assertTrue(future.isSuccess());
        Collection<HobsonDeviceDescriptor> c = dm.getDevices(HubContext.createLocal());
        assertEquals(1, c.size());
        assertEquals("did", c.iterator().next().getContext().getDeviceId());
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManagerRegistrationFailure() throws Exception {
        MockDeviceManager dm = new MockRegisterFailureDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getDevices(HubContext.createLocal()).size());
        Future future = plugin.publishDeviceProxy(new MockDeviceProxy(plugin, "did", DeviceType.LIGHTBULB));
        future.await();
        assertFalse(future.isSuccess());
    }

    @Test
    public void testGetConfigurationMetaData() {
        MockActionManager am = new MockActionManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setActionManager(am);
        assertNotNull(plugin.getDescriptor().getConfigurationClass());
        assertFalse(plugin.getDescriptor().getConfigurationClass().hasSupportedProperties());
        plugin.getDescriptor().getConfigurationClass().addSupportedProperty(new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).build());
        assertEquals(1, plugin.getDescriptor().getConfigurationClass().getSupportedProperties().size());
    }

    @Test
    public void testIsConfigurable() {
        MockActionManager am = new MockActionManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setActionManager(am);
        assertFalse(plugin.getDescriptor().isConfigurable());
        plugin.getDescriptor().getConfigurationClass().addSupportedProperty(new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).build());
        assertTrue(plugin.getDescriptor().isConfigurable());
    }

    @Test
    public void testSetDeviceConfigurationPropertyWithNoDeviceManager() {
        try {
            MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
            plugin.setDeviceConfigurationProperty(DeviceContext.createLocal("id", "id"), null, "name", true);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithNoValue() {
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setDeviceManager(dm);
        assertNull(plugin.getDeviceVariableState("id", "name"));
    }

    @Test
    public void testGetDeviceLastCheckin() {
        MockDeviceManager dm = new MockDeviceManager();
        MockEventManager em = new MockEventManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name", "1.0.0", "");
        plugin.setDeviceManager(dm);
        plugin.setEventManager(em);
        MockDeviceProxy proxy = new MockDeviceProxy(plugin, "device1", DeviceType.CAMERA);
        proxy.setLastCheckin(4000L);
        plugin.publishDeviceProxy(proxy).syncUninterruptibly();
        assertEquals(4000L, (long)plugin.getDeviceLastCheckin("device1"));
        assertNull(plugin.getDeviceLastCheckin("device2"));
    }

    @Test
    public void testOnPluginConfigurationUpdateEvent() {
        PluginContext ctx = PluginContext.createLocal("pid");
        PluginContext ctx2 = PluginContext.createLocal("pid1");
        PropertyContainer cfg = new PropertyContainer(PropertyContainerClassContext.create(ctx, "config"), null);
        PropertyContainer cfg2 = new PropertyContainer(PropertyContainerClassContext.create(ctx, "config"), null);
        MockHobsonPlugin plugin = new MockHobsonPlugin(ctx.getPluginId(), "name", "1.0.0", "");
        assertFalse(plugin.hasConfiguration());
        plugin.onPluginConfigurationUpdateEvent(new PluginConfigurationUpdateEvent(System.currentTimeMillis(), ctx2, cfg2));
        assertFalse(plugin.hasConfiguration());
        plugin.onPluginConfigurationUpdateEvent(new PluginConfigurationUpdateEvent(System.currentTimeMillis(), ctx, cfg));
        assertTrue(plugin.hasConfiguration());
    }
}
