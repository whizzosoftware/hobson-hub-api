/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.config.ConfigurationProperty;
import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.MockAbstractHobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.MockVariableManager;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AbstractHobsonDeviceTest {
    @Test
    public void testGetPluginId() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        HobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals("pid", d.getContext().getPluginId());
    }

    @Test
    public void testGetName() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        AbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        // name should default to device ID
        assertEquals("did", d.getName());
        // if default name is set, name should default to that
        d.setDefaultName("foo");
        assertEquals("foo", d.getName());
    }

    @Test
    public void testGetPreferredVariableName() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        AbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        // should be null by default
        assertNull(d.getPreferredVariableName());
    }

    @Test
    public void testStart() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");

        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.setDefaultName("deviceName");

        assertFalse(d.wasStartupCalled);
        d.onStartup(new Configuration());
        assertTrue(d.wasStartupCalled);
    }

    @Test
    public void testStop() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertFalse(d.wasShutdownCalled);
        d.onShutdown();
        assertTrue(d.wasShutdownCalled);
    }

    @Test
    public void testGetConfigurationMetaHasNameByDefault() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertNotNull(d.getConfigurationPropertyMetaData());
        assertEquals(1, d.getConfigurationPropertyMetaData().size());
        assertEquals("name", d.getConfigurationPropertyMetaData().iterator().next().getId());
    }

    @Test
    public void testAddConfigurationMeta() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertNotNull(d.getConfigurationPropertyMetaData());
        assertEquals(1, d.getConfigurationPropertyMetaData().size());
        d.addConfigurationMetaData(new ConfigurationPropertyMetaData("password", "Password", "", ConfigurationPropertyMetaData.Type.PASSWORD));
        // make sure new didn't overwrite old
        assertEquals(2, d.getConfigurationPropertyMetaData().size());
    }

    @Test
    public void testSetConfigurationProperty() {
        MockDeviceManager dm = new MockDeviceManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.getRuntime().setDeviceManager(dm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.setConfigurationProperty("prop1", "value1", true);
        assertEquals("value1", dm.configuration.get("pid.did.prop1"));
    }

    @Test
    public void testOnDeviceConfigurationUpdate() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals("did", d.getName());
        Configuration config = new Configuration();
        config.addProperty(ConfigurationProperty.create("name", "foo"));
        d.onDeviceConfigurationUpdate(config);
        assertEquals("foo", d.getName());
    }

    @Test
    public void testGetDefaultName() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals("did", d.getDefaultName());
        d.setDefaultName("foo");
        assertEquals("foo", d.getDefaultName());
    }

    @Test
    public void testGetPlugin() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals(p, d.getPlugin());
    }

    @Test
    public void testPublishVariableWithNoVariableManager() {
        try {
            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
            MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
            d.publishVariable("var1", "val1", HobsonVariable.Mask.READ_WRITE);
        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testPublishVariableWithVariableManager() {
        MockVariableManager vm = new MockVariableManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.getRuntime().setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.publishVariable("var1", "val1", HobsonVariable.Mask.READ_WRITE);
        HobsonVariable v = vm.getDeviceVariable(DeviceContext.create(p.getContext(), "did"), "var1");
        assertNotNull(v);
        assertEquals("var1", v.getName());
        assertEquals("val1", v.getValue());
        assertEquals(HobsonVariable.Mask.READ_WRITE, v.getMask());
    }

    @Test
    public void testFireVariableUpdateNotificationsWithNoVariableManager() {
        try {
            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
            MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
            d.fireVariableUpdateNotifications(new ArrayList<VariableUpdate>());
            fail("Should have thrown an exception");
        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testFireVariableUpdateNotifications() {
//        MockVariablePublisher vp = new MockVariablePublisher();
//        MockVariableManager vm = new MockVariableManager(vp);
//        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.getRuntime().setVariableManager(vm);
//        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
//        List<VariableUpdate> updates = new ArrayList<VariableUpdate>();
//        updates.add(new VariableUpdate("pid", "did", "var1", "val1"));
//        updates.add(new VariableUpdate("pid", "did", "var2", "val2"));
//        assertEquals(0, vp.getFiredUpdates().size());
//        d.fireVariableUpdateNotifications(updates);
//        assertEquals(2, vp.getFiredUpdates().size());
//        assertEquals("var1", vp.getFiredUpdates().get(0).getName());
//        assertEquals("val1", vp.getFiredUpdates().get(0).getValue());
//        assertEquals("var2", vp.getFiredUpdates().get(1).getName());
//        assertEquals("val2", vp.getFiredUpdates().get(1).getValue());
    }

    @Test
    public void testFireVariableUpdateNotificationWithNoVariableManager() {
        try {
            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
            MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
            d.fireVariableUpdateNotification("name", "value");
            fail("Should have thrown an exception");
        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testFireVariableUpdateNotification() {
//        MockVariablePublisher vp = new MockVariablePublisher();
//        MockVariableManager vm = new MockVariableManager(vp);
//        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.getRuntime().setVariableManager(vm);
//        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
//        d.fireVariableUpdateNotification("name", "value");
//        assertEquals(1, vp.getFiredUpdates().size());
//        assertEquals("pid", vp.getFiredUpdates().get(0).getPluginId());
//        assertEquals("did", vp.getFiredUpdates().get(0).getDeviceId());
//        assertEquals("name", vp.getFiredUpdates().get(0).getName());
//        assertEquals("value", vp.getFiredUpdates().get(0).getValue());
    }

    @Test
    public void testGetVariableWithNoVariableManager() {
        try {
            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
            MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
            d.getVariable("foo");
        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testGetVariableWithWithVariableManager() {
        MockVariableManager vm = new MockVariableManager();
        vm.publishDeviceVariable(DeviceContext.create(PluginContext.createLocal("pid"), "did"), "var1", "val1", HobsonVariable.Mask.READ_WRITE);
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.getRuntime().setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        HobsonVariable hv = d.getVariable("var1");
        assertNotNull(hv);
        assertEquals("var1", hv.getName());
        assertEquals("val1", hv.getValue());
    }
}
