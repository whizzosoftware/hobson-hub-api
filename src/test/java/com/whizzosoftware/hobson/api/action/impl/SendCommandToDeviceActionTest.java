package com.whizzosoftware.hobson.api.action.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import com.whizzosoftware.hobson.api.variable.VariableUpdate;

public class SendCommandToDeviceActionTest {
    @Test
    public void testCreateVariableUpdateForSetLevel() {
        SendCommandToDeviceAction a = new SendCommandToDeviceAction("pid");
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("pluginId", "com.whizzosoftware.hobson.hub.hobson-hub-zwave");
        p.put("deviceId", "zwave-40:1");
        p.put("commandId", "setLevel");
        p.put("param", 100);
        VariableUpdate v = a.createVariableUpdate(p);
        assertEquals("com.whizzosoftware.hobson.hub.hobson-hub-zwave", v.getPluginId());
        assertEquals("zwave-40:1", v.getDeviceId());
        assertEquals("level", v.getName());
        assertEquals(100, v.getValue());
    }

    @Test
    public void testCreateVariableUpdateForTurnOff() {
        SendCommandToDeviceAction a = new SendCommandToDeviceAction("pid");
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("pluginId", "com.whizzosoftware.hobson.hub.hobson-hub-zwave");
        p.put("deviceId", "zwave-40:1");
        p.put("commandId", "turnOff");
        VariableUpdate v = a.createVariableUpdate(p);
        assertEquals("com.whizzosoftware.hobson.hub.hobson-hub-zwave", v.getPluginId());
        assertEquals("zwave-40:1", v.getDeviceId());
        assertEquals("on", v.getName());
        assertEquals(false, v.getValue());
    }

    @Test
    public void testCreateVariableUpdateForTurnOn() {
        SendCommandToDeviceAction a = new SendCommandToDeviceAction("pid");
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("pluginId", "com.whizzosoftware.hobson.hub.hobson-hub-zwave");
        p.put("deviceId", "zwave-40:1");
        p.put("commandId", "turnOn");
        VariableUpdate v = a.createVariableUpdate(p);
        assertEquals("com.whizzosoftware.hobson.hub.hobson-hub-zwave", v.getPluginId());
        assertEquals("zwave-40:1", v.getDeviceId());
        assertEquals("on", v.getName());
        assertEquals(true, v.getValue());
    }

    @Test
    public void testCreateVariableUpdateForInvalidCommandId() {
        SendCommandToDeviceAction a = new SendCommandToDeviceAction("pid");
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("pluginId", "com.whizzosoftware.hobson.hub.hobson-hub-zwave");
        p.put("deviceId", "zwave-40:1");
        p.put("commandId", "ascnajdwqdqwdiq123**(&");
        try {
            a.createVariableUpdate(p);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }
}
