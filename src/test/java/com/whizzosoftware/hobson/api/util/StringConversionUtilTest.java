/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StringConversionUtilTest {
    @Test
    public void testCreateTypedValueString() {
        assertEquals("N", StringConversionUtil.createTypedValueString(null));

        assertEquals("Sfoo", StringConversionUtil.createTypedValueString("foo"));

        assertEquals("I12", StringConversionUtil.createTypedValueString(12));

        assertEquals("D4.6", StringConversionUtil.createTypedValueString(4.6));

        assertEquals("Btrue", StringConversionUtil.createTypedValueString(true));
        assertEquals("Bfalse", StringConversionUtil.createTypedValueString(false));

        assertEquals("Elocal:local:plugin:device", StringConversionUtil.createTypedValueString(DeviceContext.createLocal("plugin", "device")));

        List<DeviceContext> list = new ArrayList<>();
        list.add(DeviceContext.create(HubContext.createLocal(), "plugin1", "device1"));
        list.add(DeviceContext.create(HubContext.createLocal(), "plugin2", "device2"));
        assertEquals("Flocal:local:plugin1:device1,local:local:plugin2:device2", StringConversionUtil.createTypedValueString(list));
    }

    @Test
    public void testCastTypedValueString() {
        assertEquals("foo", StringConversionUtil.castTypedValueString("Sfoo"));
        assertEquals(12, StringConversionUtil.castTypedValueString("I12"));
        assertEquals(4.6, StringConversionUtil.castTypedValueString("D4.6"));
        assertEquals(true, StringConversionUtil.castTypedValueString("Btrue"));
        assertEquals(false, StringConversionUtil.castTypedValueString("Bfalse"));

        DeviceContext ctx = (DeviceContext)StringConversionUtil.castTypedValueString("Elocal:local:plugin:device");
        assertEquals("local", ctx.getUserId());
        assertEquals("local", ctx.getHubId());
        assertEquals("plugin", ctx.getPluginId());
        assertEquals("device", ctx.getDeviceId());

        List<DeviceContext> list = (List<DeviceContext>)StringConversionUtil.castTypedValueString("Flocal:local:plugin1:device1,local:local:plugin2:device2");
        assertEquals(2, list.size());
        assertEquals("local", list.get(0).getUserId());
        assertEquals("local", list.get(0).getHubId());
        assertEquals("plugin1", list.get(0).getPluginId());
        assertEquals("device1", list.get(0).getDeviceId());
        assertEquals("local", list.get(1).getUserId());
        assertEquals("local", list.get(1).getHubId());
        assertEquals("plugin2", list.get(1).getPluginId());
        assertEquals("device2", list.get(1).getDeviceId());
    }
}
