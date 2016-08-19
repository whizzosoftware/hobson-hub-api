/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.device.proxy.DeviceProxyVariable;
import com.whizzosoftware.hobson.api.device.proxy.MockDeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.DeviceVariable;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class AbstractHobsonPluginTest {
    @Test
    public void testPublishAndStartDeviceWithNoDeviceManager() {
        try {
            MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
            plugin.registerDeviceProxy(new MockDeviceProxy(plugin, "pid", DeviceType.LIGHTBULB));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManager() throws Exception {
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getAllDeviceDescriptions(HubContext.createLocal()).size());
        Future future = plugin.registerDeviceProxy(new MockDeviceProxy(plugin, "did", DeviceType.LIGHTBULB));
        future.await();
        assertTrue(future.isSuccess());
        Collection<DeviceDescription> c = dm.getAllDeviceDescriptions(HubContext.createLocal());
        assertEquals(1, c.size());
        assertEquals("did", c.iterator().next().getContext().getDeviceId());
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManagerRegistrationFailure() throws Exception {
        MockDeviceManager dm = new MockRegisterFailureDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getAllDeviceDescriptions(HubContext.createLocal()).size());
        Future future = plugin.registerDeviceProxy(new MockDeviceProxy(plugin, "did", DeviceType.LIGHTBULB));
        future.await();
        assertFalse(future.isSuccess());
    }

    @Test
    public void testGetConfigurationMetaData() {
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
        assertNotNull(plugin.getConfigurationClass());
        assertFalse(plugin.getConfigurationClass().hasSupportedProperties());
        plugin.getConfigurationClass().addSupportedProperty(new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).build());
        assertEquals(1, plugin.getConfigurationClass().getSupportedProperties().size());
    }

    @Test
    public void testIsConfigurable() {
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
        assertFalse(plugin.isConfigurable());
        plugin.getConfigurationClass().addSupportedProperty(new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).build());
        assertTrue(plugin.isConfigurable());
    }

    @Test
    public void testSetDeviceConfigurationPropertyWithNoConfigManager() {
        try {
            MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
            plugin.setDeviceConfigurationProperty(DeviceContext.create(plugin.getContext(), "id"), "name", true, true);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithNoValue() {
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
        plugin.setDeviceManager(dm);
        try {
            DeviceVariable v = plugin.getDeviceVariable("id", "name");
            fail("Should have thrown exception");
        } catch (HobsonNotFoundException ignored) {}
    }

//    @Test
//    public void testPublishGlobalVariableWithNoVariableManager() {
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.publishVariable(VariableContext.createGlobal(plugin.getContext(), "name"), "value", HobsonVariable.Mask.READ_WRITE, null);
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }
//
//    @Test
//    public void testPublishDeviceVariableWithNoVariableManager() {
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.publishVariable(VariableContext.create(DeviceContext.create(plugin.getContext(), "id"), "name"), "value", HobsonVariable.Mask.READ_WRITE, null);
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }

//    @Test
//    public void testFireVariableUpdateNotificationsWithNoVariableManager() {
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            List<VariableUpdate> updates = new ArrayList<VariableUpdate>();
//            plugin.fireVariableUpdateNotifications(updates);
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }
//
//    @Test
//    public void testFireVariableUpdateNotificationWithNoVariableManager() {
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.fireVariableUpdateNotification(new VariableUpdate(VariableContext.createLocal("pluginId", "deviceId", "name"), "value"));
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }

    @Test
    public void testOnDeviceConfigurationUpdateWithNoDeviceManager() {
        try {
            MockHobsonPlugin plugin = new MockHobsonPlugin("id", "name");
            plugin.onDeviceConfigurationUpdate("id", new PropertyContainer());
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

//    @Test
//    public void testStopAndUnpublishDeviceWithNoManagers() {
//        // first no device manager or variable manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.unpublishDevice("id");
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//
//        // then a device manager but no variable manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.setDeviceManager(new MockDeviceManager());
//            plugin.unpublishDevice("id");
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//
//        // then a variable manager but  no device manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.setVariableManager(new MockVariableManager());
//            plugin.unpublishDevice("id");
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }

//    @Test
//    public void testStopAndUnpublishAllDevicesWithNoManagers() {
//        // first no device manager or variable manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.unpublishAllDevices();
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//
//        // then a device manager but no variable manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.setDeviceManager(new MockDeviceManager());
//            plugin.unpublishAllDevices();
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//
//        // then a variable manager but  no device manager
//        try {
//            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
//            plugin.setVariableManager(new MockVariableManager());
//            plugin.unpublishAllDevices();
//            fail("Should have thrown exception");
//        } catch (HobsonRuntimeException ignored) {
//        }
//    }

    @Test
    public void testOnHobsonEventWithRelevantVariableUpdateEvent() {
        // create a device manager with one published device
//        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("pid", "name");
//        MockDevicePublisher dp = new MockDevicePublisher();
//        MockDeviceManager dm = new MockDeviceManager(dp);
//        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(plugin, "did");
//        dp.addPublishedDevice(d);
//
//        plugin.setDeviceManager(dm);
//
//        assertEquals(0, d.setVariableRequests.size());
//        plugin.onHobsonEvent(new VariableUpdateRequestEvent(new VariableUpdate("pid", "did", "var", "newValue")));
//        assertEquals(1, d.setVariableRequests.size());
//        assertEquals("newValue", d.setVariableRequests.get("var"));
    }

    @Test
    public void testOnHobsonEventWithIrrelevantVariableUpdateEvent() {
        // create a device manager with one published device
//        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("pid", "name");
//        MockDevicePublisher dp = new MockDevicePublisher();
//        MockDeviceManager dm = new MockDeviceManager(dp);
//        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(plugin, "did");
//        dp.addPublishedDevice(d);
//
//        plugin.setDeviceManager(dm);
//
//        assertEquals(0, d.setVariableRequests.size());
//        // note that the event is for a different plugin ID
//        plugin.onHobsonEvent(new VariableUpdateRequestEvent(new VariableUpdate("pid2", "did", "var", "newValue")));
//        assertEquals(0, d.setVariableRequests.size());
    }
}
