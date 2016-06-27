/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

public class DeviceContextTest {
    @Test
    public void testCreateCollectionSingle() {
        Collection<DeviceContext> ctxs = DeviceContext.createCollection("local:plugin1:device1");
        assertEquals(1, ctxs.size());

        Iterator<DeviceContext> it = ctxs.iterator();
        DeviceContext ctx = it.next();
        assertEquals("local", ctx.getHubId());
        assertEquals("plugin1", ctx.getPluginId());
        assertEquals("device1", ctx.getDeviceId());
    }

    @Test
    public void testCreateCollectionMultiple() {
        Collection<DeviceContext> ctxs = DeviceContext.createCollection("local:plugin1:device1,local:plugin2:device2");
        assertEquals(2, ctxs.size());

        Iterator<DeviceContext> it = ctxs.iterator();

        while (it.hasNext()) {
            DeviceContext ctx = it.next();
            assertEquals("local", ctx.getHubId());
            assertTrue("plugin1".equals(ctx.getPluginId()) || "plugin2".equals(ctx.getPluginId()));
            assertTrue("device1".equals(ctx.getDeviceId()) || "device2".equals(ctx.getDeviceId()));
        }
    }

    @Test
    public void testIsGlobal() {
        DeviceContext dctx = DeviceContext.createGlobal(PluginContext.createLocal("plugin1"));
        assertTrue(dctx.isGlobal());
    }
}
