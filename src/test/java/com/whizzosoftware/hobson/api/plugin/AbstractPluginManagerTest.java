/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.MockActionManager;
import com.whizzosoftware.hobson.api.config.ConfigurationManager;
import com.whizzosoftware.hobson.api.config.MockConfigurationManager;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.image.ImageInputStream;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AbstractPluginManagerTest {
    private MockActionManager am;
    private MockConfigurationManager cm;
    private MockAbstractPluginManager pm;
    private MyMockHobsonPlugin plugin;

    @Before
    public void setUp() {
        am = new MockActionManager();
        cm = new MockConfigurationManager();
        pm = new MockAbstractPluginManager(cm);
        plugin = new MyMockHobsonPlugin("plugin1", "Plugin", "1.0", "Description");
        plugin.setActionManager(am);
        pm.addLocalPlugin(plugin);
    }

    @Test
    public void testSetPluginConfigurationWithValidConfig() {
        Map<String,Object> values = new HashMap<>();
        values.put("username", "foo@bar.com");
        values.put("password", "password");

        pm.setLocalPluginConfiguration(plugin.getContext(), values);

        PropertyContainer config = pm.getLocalPluginConfiguration(plugin.getContext());
        assertNotNull(config);
        assertEquals("foo@bar.com", config.getStringPropertyValue("username"));
        assertEquals("password", config.getStringPropertyValue("password"));
    }

    @Test
    public void testSetPluginConfigurationWithExtraValues() {
        Map<String,Object> values = new HashMap<>();
        values.put("username", "foo@bar.com");
        values.put("password", "password");
        values.put("foo", "bar");

        try {
            pm.setLocalPluginConfiguration(plugin.getContext(), values);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {}
    }

    @Test
    public void testSetPluginConfigurationWithMissingRequiredValue() {
        Map<String,Object> values = new HashMap<>();
        values.put("password", "password");

        try {
            pm.setLocalPluginConfiguration(plugin.getContext(), values);
            fail("Should have thrown exception");
        } catch (HobsonRuntimeException ignored) {}
    }

    private class MockAbstractPluginManager extends AbstractPluginManager {
        private ConfigurationManager configManager;
        private Map<PluginContext,HobsonPlugin> pluginMap = new HashMap<>();

        public MockAbstractPluginManager(ConfigurationManager configManager) {
            this.configManager = configManager;
        }

        @Override
        public void addRemoteRepository(String uri) {

        }

        @Override
        public Long getLocalPluginDeviceLastCheckin(PluginContext ctx, String deviceId) {
            return null;
        }

        @Override
        public ImageInputStream getLocalPluginIcon(PluginContext ctx) {
            return null;
        }

        @Override
        public Collection<HobsonLocalPluginDescriptor> getLocalPlugins(HubContext ctx) {
            return null;
        }

        @Override
        public HobsonPluginDescriptor getRemotePlugin(PluginContext ctx, String version) {
            return null;
        }

        @Override
        public Collection<HobsonPluginDescriptor> getRemotePlugins(HubContext ctx) {
            return null;
        }

        @Override
        public Map<String, String> getRemotePluginVersions(HubContext ctx) {
            return null;
        }

        @Override
        public Collection<String> getRemoteRepositories() {
            return null;
        }

        @Override
        public void installRemotePlugin(PluginContext ctx, String pluginVersion) {

        }

        @Override
        public void reloadLocalPlugin(PluginContext ctx) {

        }

        @Override
        public void removeRemoteRepository(String uri) {

        }

        @Override
        protected HobsonPlugin getLocalPluginInternal(PluginContext ctx) {
            return pluginMap.get(ctx);
        }

        public void addLocalPlugin(HobsonPlugin plugin) {
            pluginMap.put(plugin.getContext(), plugin);
        }

        @Override
        protected ConfigurationManager getConfigurationManager() {
            return configManager;
        }

        @Override
        protected EventManager getEventManager() {
            return null;
        }
    }

    private class MyMockHobsonPlugin extends MockHobsonPlugin {

        public MyMockHobsonPlugin(String pluginId, String name, String version, String description) {
            super(pluginId, name, version, description);
        }

        @Override
        protected TypedProperty[] getConfigurationPropertyTypes() {
            TypedProperty[] tp = new TypedProperty[2];
            tp[0] = new TypedProperty.Builder("username", "User name", "User name", TypedProperty.Type.STRING).constraint(PropertyConstraintType.required, true).build();
            tp[1] = new TypedProperty.Builder("password", "Password", "User name", TypedProperty.Type.SECURE_STRING).build();
            return tp;
        }
    }
}
