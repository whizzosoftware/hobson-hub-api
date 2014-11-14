/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.MockConfigurationManager;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.MockAbstractHobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.HobsonVariableImpl;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.MockVariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractHobsonDeviceTest {
    @Test
    public void testGetPluginId() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        HobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals("pid", d.getPluginId());
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
        MockConfigurationManager cm = new MockConfigurationManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setConfigurationManager(cm);

        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.setDefaultName("deviceName");

        assertFalse(d.wasStartupCalled);
        d.onStartup();
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
        assertNotNull(d.getConfigurationMetaData());
        assertEquals(1, d.getConfigurationMetaData().size());
        assertEquals("name", d.getConfigurationMetaData().iterator().next().getId());
    }

    @Test
    public void testAddConfigurationMeta() {
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertNotNull(d.getConfigurationMetaData());
        assertEquals(1, d.getConfigurationMetaData().size());
        d.addConfigurationMetaData(new ConfigurationMetaData("password", "Password", "", ConfigurationMetaData.Type.PASSWORD));
        // make sure new didn't overwrite old
        assertEquals(2, d.getConfigurationMetaData().size());
    }

    @Test
    public void testSetConfigurationProperty() {
        MockConfigurationManager cm = new MockConfigurationManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setConfigurationManager(cm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.setConfigurationProperty("prop1", "value1", true);
        assertEquals("value1", cm.deviceProperties.get("pid.did.prop1"));
    }

    @Test
    public void testOnDeviceConfigurationUpdate() {
        MockConfigurationManager cm = new MockConfigurationManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setConfigurationManager(cm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        assertEquals("did", d.getName());
        Dictionary dic = new Hashtable();
        dic.put("name", "foo");
        d.onDeviceConfigurationUpdate(dic);
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
        p.setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.publishVariable("var1", "val1", HobsonVariable.Mask.READ_WRITE);
        HobsonVariable v = vm.getDeviceVariable("pid", "did", "var1");
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
        MockVariableManager vm = new MockVariableManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        List<VariableUpdate> updates = new ArrayList<VariableUpdate>();
        updates.add(new VariableUpdate("pid", "did", "var1", "val1"));
        updates.add(new VariableUpdate("pid", "did", "var2", "val2"));
        assertEquals(0, vm.firedUpdates.size());
        d.fireVariableUpdateNotifications(updates);
        assertEquals(2, vm.firedUpdates.size());
        assertEquals("var1", vm.firedUpdates.get(0).getName());
        assertEquals("val1", vm.firedUpdates.get(0).getValue());
        assertEquals("var2", vm.firedUpdates.get(1).getName());
        assertEquals("val2", vm.firedUpdates.get(1).getValue());
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
        MockVariableManager vm = new MockVariableManager();
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        d.fireVariableUpdateNotification("name", "value");
        assertEquals(1, vm.firedUpdates.size());
        assertEquals("pid", vm.firedUpdates.get(0).getPluginId());
        assertEquals("did", vm.firedUpdates.get(0).getDeviceId());
        assertEquals("name", vm.firedUpdates.get(0).getName());
        assertEquals("value", vm.firedUpdates.get(0).getValue());
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
        vm.publishDeviceVariable("pid", "did", new HobsonVariableImpl("var1", "val1", HobsonVariable.Mask.READ_WRITE));
        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
        p.setVariableManager(vm);
        MockAbstractHobsonDevice d = new MockAbstractHobsonDevice(p, "did");
        HobsonVariable hv = d.getVariable("var1");
        assertNotNull(hv);
        assertEquals("var1", hv.getName());
        assertEquals("val1", hv.getValue());
    }
}
