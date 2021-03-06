/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.event.device.DeviceAvailableEvent;
import com.whizzosoftware.hobson.api.event.device.DeviceVariablesUpdateEvent;
import com.whizzosoftware.hobson.api.event.MockEventManager;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.MockHobsonPlugin;
import com.whizzosoftware.hobson.api.variable.DeviceVariableDescriptor;
import com.whizzosoftware.hobson.api.variable.DeviceVariableUpdate;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.api.variable.VariableMask;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AbstractHobsonDeviceProxyTest {
    @Test
    public void testGetPluginId() {
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0", "description");
        p.setDeviceManager(new MockDeviceManager());
        HobsonDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
        assertEquals("pid", d.getContext().getPluginId());
    }

    @Test
    public void testGetName() {
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0", "description");
        p.setDeviceManager(new MockDeviceManager());
        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB, "defaultName");
        // name should default to default name
        assertEquals("defaultName", d.getName());
        // if default name is set, name should default to that
        d.start("foo", null);
        assertEquals("foo", d.getName());
    }

    @Test
    public void testGetPreferredVariableName() {
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0", "");
        p.setDeviceManager(new MockDeviceManager());
        AbstractHobsonDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
        // should be null by default
        assertNull(d.getDescriptor().getPreferredVariableName());
    }

    @Test
    public void testStart() throws Exception {
        MockEventManager em = new MockEventManager();
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0", "");
        p.setDeviceManager(dm);
        p.setEventManager(em);

        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB, "deviceName") {
            public void onStartup(String name, Map<String,Object> config) {
                setLastCheckin(2000L);
                publishVariables(createDeviceVariable("foo", VariableMask.READ_WRITE));
            }

            public String getPreferredVariableName() {
                return "pname";
            }

            public String getManufacturerName() {
                return "manufacturer";
            }

            public String getManufacturerVersion() {
                return "mversion";
            }
        };
        p.publishDeviceProxy(d).sync();

        assertFalse(d.isStarted());
        d.start("name", new HashMap<String,Object>());
        assertTrue(d.isStarted());

        // device manager is in charge of flagging the device as available, so make sure it is still unavailable here
        assertFalse(dm.isDeviceAvailable(DeviceContext.create(p.getContext(), d.getContext().getDeviceId())));

        HobsonDeviceDescriptor device = dm.getDevice(DeviceContext.create(p.getContext(), "did"));
        assertNotNull(device);
        assertEquals("name", device.getName());
        assertEquals(DeviceType.LIGHTBULB, device.getType());
        assertEquals("manufacturer", device.getManufacturerName());
        assertEquals("mversion", device.getManufacturerVersion());
        assertEquals("pname", device.getPreferredVariableName());
        assertNotNull(device.getVariable("foo"));
    }

    @Test
    public void testStop() {
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0", "");
        p.setDeviceManager(new MockDeviceManager());
        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
        assertFalse(d.wasShutdownCalled);
        d.onShutdown();
        assertTrue(d.wasShutdownCalled);
    }

    @Test
    public void testGetConfigurationMetaHasNameByDefault() {
//        MockAbstractHobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.setDeviceManager(new MockDeviceManager());
//        MockDeviceProxy d = new MockDeviceProxy(p, "did");
//        assertNotNull(d.getDescription().getConfigurationClass());
//        assertEquals(1, d.getDescription().getConfigurationClass().getSupportedProperties().size());
//        assertEquals("name", d.getDescription().getConfigurationClass().getSupportedProperties().get(0).getId());
    }

    @Test
    public void testSetConfigurationProperty() {
//        MockDeviceManager dm = new MockDeviceManager();
//        HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.getRuntime().setDeviceManager(dm);
//        MockDeviceProxy d = new MockDeviceProxy(p, "did");
//        d.setConfigurationProperty("prop1", "value1", true);
//        assertEquals("value1", dm.configuration.get("pid.did.prop1"));
    }

    @Test
    public void testOnDeviceConfigurationUpdate() {
//        MockAbstractHobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.setDeviceManager(new MockDeviceManager());
//        MockDeviceProxy d = new MockDeviceProxy(p, "did");
//        assertEquals("did", d.getDescription().getName());
//        PropertyContainer config = new PropertyContainer();
//        config.setPropertyValue("name", "foo");
//        d.onDeviceConfigurationUpdate(config);
//        assertEquals("foo", d.getDescription().getName());
    }

    @Test
    public void testGetDefaultName() {
        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0", "");
        p.setDeviceManager(new MockDeviceManager());
        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB, "foo");
        assertEquals("foo", d.getDefaultName());
    }

//    @Test
//    public void testGetPlugin() {
//        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name");
//        p.setDeviceManager(new MockDeviceManager());
//        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
//        assertEquals(p, d.getPlugin());
//    }

    @Test
    public void testPublishVariable() {
        try {
            MockEventManager em = new MockEventManager();
            HobsonPlugin p = new MockHobsonPlugin("pid", "name", "version", "description");
            p.setEventManager(em);
            MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
            assertEquals(0, em.getPostedEvents().size());
            d.publishVariables(d.createDeviceVariable("var1", VariableMask.READ_WRITE, "val1", null));
            assertEquals(1, em.getPostedEvents().size());
            assertTrue(em.getPostedEvents().get(0) instanceof DeviceVariablesUpdateEvent);
            DeviceVariablesUpdateEvent dvue = (DeviceVariablesUpdateEvent)em.getPostedEvents().get(0);
            assertEquals(1, dvue.getUpdates().size());
            DeviceVariableUpdate du = dvue.getUpdates().iterator().next();
            assertEquals("pid", du.getPluginId());
            assertEquals("did", du.getDeviceId());
            assertEquals("var1", du.getName());
        } catch (HobsonRuntimeException ignored) {}
    }

//    @Test
//    public void testPublishVariableWithVariableManager() {
//        MockVariableManager vm = new MockVariableManager();
//        MockAbstractHobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//        p.setDeviceManager(new MockDeviceManager());
//        p.getRuntime().setVariableManager(vm);
//        MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
//        d.publishVariable("var1", "val1", HobsonVariable.Mask.READ_WRITE, null);
//        HobsonVariable v = vm.getVariable(VariableContext.create(p.getContext(), "did", "var1"));
//        assertNotNull(v);
//        assertEquals("var1", v.getName());
//        assertEquals("val1", v.getValue());
//        assertEquals(HobsonVariable.Mask.READ_WRITE, v.getMask());
//    }

//    @Test
//    public void testFireVariableUpdateNotificationsWithNoVariableManager() {
//        try {
//            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//            MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
//            d.fireVariableUpdateNotifications(new ArrayList<VariableUpdate>());
//            fail("Should have thrown an exception");
//        } catch (HobsonRuntimeException ignored) {}
//    }

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

//    @Test
//    public void testFireVariableUpdateNotificationWithNoVariableManager() {
//        try {
//            HobsonPlugin p = new MockAbstractHobsonPlugin("pid", "name");
//            MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
//            d.fireVariableUpdateNotification("name", "value");
//            fail("Should have thrown an exception");
//        } catch (HobsonRuntimeException ignored) {}
//    }

    @Test
    public void testSetVariable() {
        long now = System.currentTimeMillis();
        MockEventManager em = new MockEventManager();
        MockDeviceManager dm = new MockDeviceManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("pid", "plugin", "1.0.0", "");
        plugin.setEventManager(em);
        plugin.setDeviceManager(dm);
        MockLightbulbDeviceProxy proxy = new MockLightbulbDeviceProxy(plugin, "did");
        proxy.start(null, null);
        assertEquals(1, em.getPostedEvents().size());
        em.clearPostedEvents();
        assertEquals(0, em.getPostedEvents().size());
        proxy.setVariableValue(VariableConstants.ON, false, now);
        assertEquals(1, em.getPostedEvents().size());
        DeviceVariableUpdate u = ((DeviceVariablesUpdateEvent)em.getPostedEvents().get(0)).getUpdates().iterator().next();
        assertEquals(1, ((DeviceVariablesUpdateEvent)em.getPostedEvents().get(0)).getUpdates().size());
        assertEquals("pid", u.getPluginId());
        assertEquals("did", u.getDeviceId());
        assertEquals(VariableConstants.ON, u.getName());
        assertNull(u.getOldValue());
        assertEquals(false, u.getNewValue());
        em.clearPostedEvents();
        proxy.setVariableValue(VariableConstants.ON, true, now);
        assertEquals(1, em.getPostedEvents().size());
        assertEquals(1, ((DeviceVariablesUpdateEvent)em.getPostedEvents().get(0)).getUpdates().size());
        u = ((DeviceVariablesUpdateEvent)em.getPostedEvents().get(0)).getUpdates().iterator().next();
        assertEquals("pid", u.getPluginId());
        assertEquals("did", u.getDeviceId());
        assertEquals(VariableConstants.ON, u.getName());
        assertEquals(false, u.getOldValue());
        assertEquals(true, u.getNewValue());
    }

    @Test
    public void testDeviceVariableDescriptions() {
        MockDeviceManager dm = new MockDeviceManager();
        MockEventManager em = new MockEventManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("pid", "plugin", "1.0.0", "");
        plugin.setDeviceManager(dm);
        plugin.setEventManager(em);
        MockLightbulbDeviceProxy proxy = new MockLightbulbDeviceProxy(plugin, "did");
        proxy.onStartup(null, null);
        HobsonDeviceDescriptor hvd = proxy.getDescriptor();
        assertNotNull(hvd);
        assertTrue(hvd.hasVariables());
        assertEquals(1, hvd.getVariables().size());
        DeviceVariableDescriptor dvd = hvd.getVariables().iterator().next();
        assertEquals(VariableConstants.ON, dvd.getContext().getName());
        assertEquals(VariableMask.READ_WRITE, dvd.getMask());
    }

    @Test
    public void testGetVariableWithNoDeviceManager() {
//        try {
//            HobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0");
//            MockDeviceProxy d = new MockDeviceProxy(p, "did", DeviceType.LIGHTBULB);
//            d.getVariable("foo").getValue();
//        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testGetVariableWithDeviceManager() {
//        long now = System.currentTimeMillis();
//        MockEventManager em = new MockEventManager();
//        MockDeviceManager dm = new MockDeviceManager();
//        MockHobsonPlugin p = new MockHobsonPlugin("pid", "name", "1.0.0");
//        p.setDeviceManager(dm);
//        p.setEventManager(em);
//        MockLightbulbDeviceProxy d = new MockLightbulbDeviceProxy(p, "did");
//        d.onStartup("name", null);
//        d.setVariableValue(VariableConstants.ON, true, now);
//        DeviceVariable hv = d.getVariable(VariableConstants.ON);
//        assertNotNull(hv);
//        assertEquals(VariableConstants.ON, hv.getContext().getName());
//        assertEquals(true, hv.getValue());
//        assertEquals(now, (long)hv.getLastUpdate());
    }

    @Test
    public void testLastCheckin() {
        MockDeviceManager dm = new MockDeviceManager();
        MockEventManager em = new MockEventManager();
        MockHobsonPlugin plugin = new MockHobsonPlugin("pid", "plugin", "1.0.0", "");
        plugin.setDeviceManager(dm);
        plugin.setEventManager(em);
        MockLightbulbDeviceProxy proxy = new MockLightbulbDeviceProxy(plugin, "did");

        long start = System.currentTimeMillis();

        assertEquals(0, em.getPostedEvents().size());
        proxy.setLastCheckin(start);
        assertEquals(1, em.getPostedEvents().size());
        assertTrue(em.getPostedEvents().get(0) instanceof DeviceAvailableEvent);
        em.clearPostedEvents();

        proxy.setLastCheckin(start + HobsonDeviceDescriptor.AVAILABILITY_TIMEOUT_INTERVAL - 1);
        assertEquals(0, em.getPostedEvents().size());

        proxy.setLastCheckin(start + HobsonDeviceDescriptor.AVAILABILITY_TIMEOUT_INTERVAL * 2);
        assertEquals(1, em.getPostedEvents().size());
        assertTrue(em.getPostedEvents().get(0) instanceof DeviceAvailableEvent);
    }

    private class MockLightbulbDeviceProxy extends MockDeviceProxy {
        public MockLightbulbDeviceProxy(HobsonPlugin plugin, String id) {
            super(plugin, id, DeviceType.LIGHTBULB);
        }

        @Override
        public void onStartup(String name, Map<String,Object> config) {
            publishVariables(createDeviceVariable(VariableConstants.ON, VariableMask.READ_WRITE));
        }
    }
}
