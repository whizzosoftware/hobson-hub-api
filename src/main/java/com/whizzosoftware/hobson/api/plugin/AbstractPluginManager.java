/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.ConfigurationManager;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.event.plugin.PluginConfigurationUpdateEvent;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import com.whizzosoftware.hobson.api.variable.GlobalVariable;
import com.whizzosoftware.hobson.api.variable.GlobalVariableContext;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

/**
 * An abstract implementation of PluginManager.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractPluginManager implements PluginManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    abstract protected HobsonPlugin getLocalPluginInternal(PluginContext ctx);
    abstract protected ConfigurationManager getConfigurationManager();
    abstract protected EventManager getEventManager();

    @Override
    public HobsonLocalPluginDescriptor getLocalPlugin(PluginContext ctx) {
        return getLocalPluginInternal(ctx).getDescriptor();
    }

    @Override
    public PropertyContainer getLocalPluginConfiguration(PluginContext ctx) {
        return getConfigurationManager().getLocalPluginConfiguration(ctx, getLocalPlugin(ctx).getConfigurationClass());
    }

    @Override
    public void setLocalPluginConfiguration(PluginContext ctx, PropertyContainer newConfig) {
        getConfigurationManager().setLocalPluginConfiguration(ctx, newConfig);
        postPluginConfigurationUpdateEvent(ctx);
    }

    @Override
    public void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value) {
        getConfigurationManager().setLocalPluginConfigurationProperty(ctx, name, value);
        postPluginConfigurationUpdateEvent(ctx);
    }

    @Override
    public Future setLocalPluginDeviceVariable(final DeviceVariableContext ctx, final Object value) {
        final HobsonPlugin plugin = getLocalPluginInternal(ctx.getPluginContext());
        return plugin.getEventLoopExecutor().executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                plugin.onSetDeviceVariable(ctx.getDeviceId(), ctx.getName(), value);
            }
        });
    }

    @Override
    public Future startPluginDevice(final HobsonDeviceProxy device, final String name, final PropertyContainer config, final Runnable runnable) {
        HobsonPlugin plugin = getLocalPluginInternal(device.getContext().getPluginContext());
        return plugin.getEventLoopExecutor().executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                // invoke device's onStartup callback
                device.start(name, config);

                // invoke follow-on
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    @Override
    public File getDataDirectory(PluginContext ctx) {
        File f = new File(System.getProperty(ConfigurationManager.HOBSON_HOME, "."), "data");
        if (!f.exists()) {
            if (!f.mkdir()) {
                logger.error("Error creating data directory");
            }
        }
        return f;
    }

    @Override
    public File getDataFile(PluginContext ctx, String filename) {
        return new File(getDataDirectory(ctx), (ctx != null ? (ctx.getPluginId() + "$") : "") + filename);
    }

    @Override
    public GlobalVariable getGlobalVariable(GlobalVariableContext gvctx) {
        return null;
    }

    @Override
    public Collection<GlobalVariable> getGlobalVariables(PluginContext pctx) {
        return null;
    }

    @Override
    public DeviceVariableState getLocalPluginDeviceVariable(DeviceVariableContext ctx) {
        return getLocalPluginInternal(ctx.getPluginContext()).getDeviceVariableState(ctx.getDeviceContext().getDeviceId(), ctx.getName());
    }

    private void postPluginConfigurationUpdateEvent(PluginContext ctx) {
        // post event
        getEventManager().postEvent(
                ctx.getHubContext(),
                new PluginConfigurationUpdateEvent(
                        System.currentTimeMillis(),
                        ctx,
                        getLocalPluginConfiguration(ctx)
                )
        );
    }
}
