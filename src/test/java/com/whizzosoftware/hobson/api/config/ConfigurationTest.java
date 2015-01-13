/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Dictionary;
import java.util.Map;

public class ConfigurationTest {
    @Test
    public void testGetPropertyDictionaryWithValidValue() {
        Configuration c = new Configuration();
        c.addProperty(new ConfigurationProperty(new ConfigurationPropertyMetaData("foo"), "bar"));
        Dictionary d = c.getPropertyDictionary();
        assertEquals(1, d.size());
        assertEquals("bar", d.get("foo"));
    }

    @Test
    public void testGetPropertyDictionaryWithNullValue() {
        Configuration c = new Configuration();
        c.addProperty(new ConfigurationProperty(new ConfigurationPropertyMetaData("foo"), null));
        Dictionary d = c.getPropertyDictionary();
        assertEquals(0, d.size());
    }

    @Test
    public void testGetPropertyMap() {
        Configuration c = new Configuration();
        c.addProperty(new ConfigurationProperty(new ConfigurationPropertyMetaData("foo"), "bar"));
        Map<String,Object> map = c.getPropertyMap();
        assertEquals(1, map.size());
        assertEquals("bar", map.get("foo"));
    }

    @Test
    public void testGetPropertyMapWithNullValue() {
        Configuration c = new Configuration();
        c.addProperty(new ConfigurationProperty(new ConfigurationPropertyMetaData("foo"), null));
        Map<String,Object> map = c.getPropertyMap();
        assertEquals(0, map.size());
    }
}
