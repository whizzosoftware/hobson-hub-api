/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalVariableContextTest {
    @Test
    public void testEquals() {
        GlobalVariableContext gvc1 = GlobalVariableContext.create(PluginContext.createLocal("plugin1"), "sunset");
        GlobalVariableContext gvc2 = GlobalVariableContext.create(PluginContext.createLocal("plugin1"), "sunset");
        assertEquals(gvc1, gvc2);
    }

    @Test
    public void testHashCode() {
        GlobalVariableContext gvc1 = GlobalVariableContext.create(PluginContext.createLocal("plugin1"), "sunset");
        GlobalVariableContext gvc2 = GlobalVariableContext.create(PluginContext.createLocal("plugin1"), "sunset");
        assertEquals(gvc1.hashCode(), gvc2.hashCode());
    }
}
