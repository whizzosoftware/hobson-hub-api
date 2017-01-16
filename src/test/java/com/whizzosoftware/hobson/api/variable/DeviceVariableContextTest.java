/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeviceVariableContextTest {
    @Test
    public void testIdConstructor() {
        HubContext hctx = HubContext.create("hub1");
        DeviceVariableContext vctx = DeviceVariableContext.create(hctx, "plugin1", "device1", "foo");

        vctx = DeviceVariableContext.create(vctx.toString());

        assertEquals("hub1", vctx.getHubId());
        assertEquals("plugin1", vctx.getPluginId());
        assertEquals("device1", vctx.getDeviceId());
        assertEquals("foo", vctx.getName());
    }

    @Test
    public void testCreateWithVariableId() {
        DeviceVariableContext vctx = DeviceVariableContext.create(null);
        assertNull(vctx);
    }
}
