/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PropertyContainerClassContextTest {
    @Test
    public void testHash() {
        assertTrue(PropertyContainerClassContext.create(PluginContext.createLocal("p1"), "a1").equals(PropertyContainerClassContext.create(PluginContext.createLocal("p1"), "a1")));
        assertFalse(PropertyContainerClassContext.create(PluginContext.createLocal("p1"), "a1").equals(PropertyContainerClassContext.create(PluginContext.createLocal("p2"), "a2")));

        Map<PropertyContainerClassContext,String> map = new HashMap<>();
        map.put(PropertyContainerClassContext.create(PluginContext.createLocal("p1"), "a1"), "foo");
        assertEquals("foo", map.get(PropertyContainerClassContext.create(PluginContext.createLocal("p1"), "a1")));
    }
}
