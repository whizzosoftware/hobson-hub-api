/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.property;

import com.whizzosoftware.hobson.api.HobsonInvalidRequestException;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PropertyContainerClassTest {
    @Test
    public void testSupportedPropertyOrder() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("c", "c", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());
        props.add(new TypedProperty.Builder("b", "b", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());
        props.add(new TypedProperty.Builder("a", "a", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());
        props.add(new TypedProperty.Builder("d", "d", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());
        PropertyContainerClass pcc = new PropertyContainerClass(PropertyContainerClassContext.create(HubContext.createLocal(), "cc1"), PropertyContainerClassType.DEVICE_CONFIG, props);

        Iterator<TypedProperty> it = pcc.getSupportedProperties().iterator();
        assertEquals("c", it.next().getId());
        assertEquals("b", it.next().getId());
        assertEquals("a", it.next().getId());
        assertEquals("d", it.next().getId());
    }

    @Test
    public void testEvaluatePropertyConstraintsWithNoProperties() {
        PropertyContainerClass pcc = new PropertyContainerClass(PropertyContainerClassContext.create(HubContext.createLocal(), "cc1"), PropertyContainerClassType.DEVICE_CONFIG, null);
        assertTrue(pcc.evaluatePropertyConstraints(null));
    }

    @Test
    public void testValidateWithSupportedProperties() {
        PropertyContainerClassContext ctx = PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1");

        PropertyContainerClass ac = new PropertyContainerClass(ctx, PropertyContainerClassType.ACTION);
        ac.addSupportedProperty(new TypedProperty.Builder("name", "name", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());
        ac.addSupportedProperty(new TypedProperty.Builder("port", "port", "desc", TypedProperty.Type.NUMBER).constraint(PropertyConstraintType.required, true).build());
        ac.addSupportedProperty(new TypedProperty.Builder("desc", "desc", "desc", TypedProperty.Type.STRING).build());
        ac.addSupportedProperty(new TypedProperty.Builder("enabled", "enabled", "desc", TypedProperty.Type.BOOLEAN).build());

        Map<String,Object> values = new HashMap<>();
        values.put("name", "Test Name");
        values.put("port", 8888);
        values.put("desc", "Test Description");
        values.put("enabled", true);

        // test valid
        ac.validate(values);

        // test still valid
        values.remove("desc");
        ac.validate(values);

        // test invalid (missing)
        values.remove("port");
        try {
            ac.validate(values);
            fail("Should have thrown exception");
        } catch (HobsonInvalidRequestException ignored) {}

        // test invalid (not a number)
        values.put("port", "8888");
        try {
            ac.validate(values);
            fail("Should have thrown exception");
        } catch (HobsonInvalidRequestException ignored) {}

        // test invalid (not a boolean)
        values.put("port", 8888);
        values.put("enabled", "true");
        try {
            ac.validate(values);
            fail("Should have thrown exception");
        } catch (HobsonInvalidRequestException ignored) {}

        // test invalid (not a string)
        values.put("enabled", true);
        values.put("name", 1000);
        try {
            ac.validate(values);
            fail("Should have thrown exception");
        } catch (HobsonInvalidRequestException ignored) {}
    }

    @Test
    public void testValidateWithoutSupportedProperties() {
        PropertyContainerClassContext ctx = PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1");
        PropertyContainerClass ac = new PropertyContainerClass(ctx, PropertyContainerClassType.ACTION);
        ac.validate(new HashMap<String,Object>());
    }

    @Test
    public void testValidateWithExtraProperties() {
        PropertyContainerClassContext pcctx = PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1");
        PropertyContainerClass pcc = new PropertyContainerClass(pcctx, PropertyContainerClassType.PLUGIN_CONFIG);
        pcc.addSupportedProperty(new TypedProperty.Builder("name", "name", "desc", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build());

        Map<String,Object> values = new HashMap<>();
        values.put("name", "Test Name");
        values.put("port", 8888);

        try {
            pcc.validate(values);
            fail("Should have thrown exception");
        } catch (HobsonInvalidRequestException ignored) {}
    }
}
