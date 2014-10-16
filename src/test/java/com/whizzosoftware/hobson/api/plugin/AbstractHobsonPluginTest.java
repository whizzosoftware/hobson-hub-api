/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.device.MockAbstractHobsonDevice;
import com.whizzosoftware.hobson.api.device.manager.MockDeviceManager;
import com.whizzosoftware.hobson.api.disco.MockExternalBridgeMetaAnalyzer;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.HobsonVariableImpl;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.MockVariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractHobsonPluginTest {
    @Test
    public void testPublishAndStartDeviceWithNoDeviceManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishAndStartDevice(new MockAbstractHobsonDevice(plugin, "pid"));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishAndStartDeviceWithDeviceManager() {
        MockDeviceManager dm = new MockDeviceManager();
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        plugin.setDeviceManager(dm);
        assertEquals(0, dm.getAllDevices().size());
        plugin.publishAndStartDevice(new MockAbstractHobsonDevice(plugin, "did"));
        assertEquals(1, dm.getAllDevices().size());
        assertEquals("did", dm.getAllDevices().iterator().next().getId());
    }

    @Test
    public void testGetConfigurationMetaData() {
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        assertNotNull(plugin.getConfigurationMetaData());
        assertEquals(0, plugin.getConfigurationMetaData().size());
        plugin.addConfigurationMetaData(new ConfigurationMetaData("id", "name", "desc", ConfigurationMetaData.Type.STRING));
        assertEquals(1, plugin.getConfigurationMetaData().size());
    }

    @Test
    public void testIsConfigurable() {
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        assertFalse(plugin.isConfigurable());
        plugin.addConfigurationMetaData(new ConfigurationMetaData("id", "name", "desc", ConfigurationMetaData.Type.STRING));
        assertTrue(plugin.isConfigurable());
    }

    @Test
    public void testSetDeviceConfigurationPropertyWithNoConfigManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceConfigurationProperty("id", "name", true, true);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.getDeviceVariable("id", "name");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testGetDeviceVariableWithVariableManager() {
        MockVariableManager vm = new MockVariableManager();
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
        plugin.setVariableManager(vm);
        HobsonVariable v = plugin.getDeviceVariable("id", "name");
        assertNull(v);
    }

    @Test
    public void testPublishGlobalVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishGlobalVariable(new HobsonVariableImpl("name", "value", HobsonVariable.Mask.READ_WRITE));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testPublishDeviceVariableWithNoVariableManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.publishDeviceVariable("id", new HobsonVariableImpl("name", "value", HobsonVariable.Mask.READ_WRITE));
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
            plugin.fireVariableUpdateNotification(new VariableUpdate("id", "name", "value"));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testOnDeviceConfigurationUpdateWithNoDeviceManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.onDeviceConfigurationUpdate("id", new Hashtable());
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testStopAndUnpublishDeviceWithNoManagers() {
        // first no device manager or variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.stopAndUnpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a device manager but no variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceManager(new MockDeviceManager());
            plugin.stopAndUnpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a variable manager but  no device manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setVariableManager(new MockVariableManager());
            plugin.stopAndUnpublishDevice("id");
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testStopAndUnpublishAllDevicesWithNoManagers() {
        // first no device manager or variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.stopAndUnpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a device manager but no variable manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setDeviceManager(new MockDeviceManager());
            plugin.stopAndUnpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }

        // then a variable manager but  no device manager
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("id", "name");
            plugin.setVariableManager(new MockVariableManager());
            plugin.stopAndUnpublishAllDevices();
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {
        }
    }

    @Test
    public void testOnHobsonEventWithRelevantVariableUpdateEvent() {
        // create a device manager with one published device
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("pid", "name");
        MockDeviceManager dm = new MockDeviceManager();
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(plugin, "did");
        dm.publishedDevices.add(d);

        plugin.setDeviceManager(dm);

        assertEquals(0, d.setVariableRequests.size());
        plugin.onHobsonEvent(new VariableUpdateRequestEvent("pid", "did", "var", "newValue"));
        assertEquals(1, d.setVariableRequests.size());
        assertEquals("newValue", d.setVariableRequests.get("var"));
    }

    @Test
    public void testOnHobsonEventWithIrrelevantVariableUpdateEvent() {
        // create a device manager with one published device
        MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("pid", "name");
        MockDeviceManager dm = new MockDeviceManager();
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(plugin, "did");
        dm.publishedDevices.add(d);

        plugin.setDeviceManager(dm);

        assertEquals(0, d.setVariableRequests.size());
        // note that the event is for a different plugin ID
        plugin.onHobsonEvent(new VariableUpdateRequestEvent("pid2", "did", "var", "newValue"));
        assertEquals(0, d.setVariableRequests.size());
    }

    @Test
    public void testPublishExternalBridgeMetaAnalyzerWithNoDiscoManager() {
        try {
            MockAbstractHobsonPlugin plugin = new MockAbstractHobsonPlugin("pid", "name");
            plugin.publishExternalBridgeMetaAnalyzer(new MockExternalBridgeMetaAnalyzer("pid", "id"));
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {}
    }
}
