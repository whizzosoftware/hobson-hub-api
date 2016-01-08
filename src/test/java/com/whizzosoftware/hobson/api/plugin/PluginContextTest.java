/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class PluginContextTest {
    @Test
    public void testEquals() {
        HubContext hctx = HubContext.createLocal();
        assertEquals(PluginContext.create(hctx, "plugin1"), PluginContext.create(hctx, "plugin1"));
        assertEquals(PluginContext.create(hctx, null), PluginContext.create(hctx, null));
    }
}
