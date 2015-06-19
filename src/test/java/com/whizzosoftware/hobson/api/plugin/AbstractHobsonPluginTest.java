/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.MockAbstractHobsonDevice;
import com.whizzosoftware.hobson.api.device.MockDeviceManager;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.MockVariableManager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractHobsonPluginTest {
    @Test
    public void testPublishAndStartDeviceWithNoDeviceManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishDevice(new MockAbstractHobsonDevice(plugin, "pid"));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManager() {
        MockDeviceManager dm = new MockDeviceManager();
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getAllDevices(HubContext.createLocal()).size());
        plugin.publishDevice(new MockAbstractHobsonDevice(plugin, "did"));
        assertEquals(1, dm.getAllDevices(HubContext.createLocal()).size());
        assertEquals("did", dm.getAllDevices(HubContext.createLocal()).iterator().next().getContext().getDeviceId());
    }

    @Test
    public void testGetConfigurationMetaData() {
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        assertNotNull(plugin.getConfigurationClass());
        assertFalse(plugin.getConfigurationClass().hasSupportedProperties());
        plugin.getConfigurationClass().addSupportedProperty(new TypedProperty("id", "name", "desc", TypedProperty.Type.STRING));
        assertEquals(1, plugin.getConfigurationClass().getSupportedProperties().size());
    }

    @Test
    public void testIsConfigurable() {
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        assertFalse(plugin.isConfigurable());
        plugin.getConfigurationClass().addSupportedProperty(new TypedProperty("id", "name", "desc", TypedProperty.Type.STRING));
        assertTrue(plugin.isConfigurable());
    }

    @Test
    public void testSetDeviceConfigurationPropertyWithNoConfigManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceConfigurationProperty(DeviceContext.create(plugin.getContext(), "id"), "name", true, true);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.getDeviceVariable(DeviceContext.create(plugin.getContext(), "id"), "name");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithVariableManager() {
        MockVariableManager vm = new MockVariableManager();
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        plugin.setVariableManager(vm);
        HobsonVariable v = plugin.getDeviceVariable(DeviceContext.create(plugin.getContext(), "id"), "name");
        assertNull(v);
    }

    @Test
    public void testPublishGlobalVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishGlobalVariable("name", "value", HobsonVariable.Mask.READ_WRITE);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishDeviceVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishDeviceVariable(DeviceContext.create(plugin.getContext(), "id"), "name", "value", HobsonVariable.Mask.READ_WRITE);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testFireVariableUpdateNotificationsWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            List<VariableUpdate> updates = new ArrayList<VariableUpdate>();
            plugin.fireVariableUpdateNotifications(updates);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testFireVariableUpdateNotificationWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.fireVariableUpdateNotification(new VariableUpdate(DeviceContext.createLocal("pluginId", "deviceId"), "name", "value"));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testOnDeviceConfigurationUpdateWithNoDeviceManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.onDeviceConfigurationUpdate(DeviceContext.create(plugin.getContext(), "id"), new PropertyContainer());
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testStopAndUnpublishDeviceWithNoManagers() {
        // first no device manager or variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.unpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a device manager but no variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceManager(new MockDeviceManager());
            plugin.unpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a variable manager but  no device manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setVariableManager(new MockVariableManager());
            plugin.unpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testStopAndUnpublishAllDevicesWithNoManagers() {
        // first no device manager or variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.unpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a device manager but no variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceManager(new MockDeviceManager());
            plugin.unpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a variable manager but  no device manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setVariableManager(new MockVariableManager());
            plugin.unpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

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
