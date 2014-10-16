/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api;

import com.whizzosoftware.hobson.api.action.impl.SendCommandToDeviceAction;
import com.whizzosoftware.hobson.api.action.impl.EmailAction;
import com.whizzosoftware.hobson.api.action.impl.LogAction;
import com.whizzosoftware.hobson.api.action.manager.ActionManager;
import com.whizzosoftware.hobson.api.action.manager.OSGIActionManager;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.config.manager.OSGIConfigurationManager;
import com.whizzosoftware.hobson.api.device.manager.DeviceManager;
import com.whizzosoftware.hobson.api.device.manager.OSGIDeviceManager;
import com.whizzosoftware.hobson.api.disco.manager.DiscoManager;
import com.whizzosoftware.hobson.api.disco.manager.OSGIDiscoManager;
import com.whizzosoftware.hobson.api.event.manager.OSGIEventManager;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.trigger.manager.OSGITriggerManager;
import com.whizzosoftware.hobson.api.trigger.manager.TriggerManager;
import com.whizzosoftware.hobson.api.variable.manager.OSGIVariableManager;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import org.apache.felix.dm.Component;
import org.apache.felix.dm.ComponentStateListener;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundle activator. This is responsible for registering all the "Manager" objects with the OSGi runtime.
 *
 * @author Dan Noguerol
 */
public class Activator extends DependencyActivatorBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Component deviceManagerComponent;
    private Component variableManagerComponent;
    private Component triggerManagerComponent;
    private Component eventManagerComponent;
    private Component configManagerComponent;
    private Component actionManagerComponent;
    private Component discoManagerComponent;

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        // register config manager
        configManagerComponent = manager.createComponent();
        configManagerComponent.setInterface(ConfigurationManager.class.getName(), null);
        configManagerComponent.setImplementation(OSGIConfigurationManager.class);
        configManagerComponent.add(createServiceDependency().setService(ConfigurationAdmin.class).setRequired(true));
        manager.add(configManagerComponent);

        // register event manager
        eventManagerComponent = manager.createComponent();
        eventManagerComponent.setInterface(EventManager.class.getName(), null);
        eventManagerComponent.setImplementation(OSGIEventManager.class);
        eventManagerComponent.add(createServiceDependency().setService(EventAdmin.class).setRequired(true));
        manager.add(eventManagerComponent);

        // register device manager
        deviceManagerComponent = manager.createComponent();
        deviceManagerComponent.setInterface(DeviceManager.class.getName(), null);
        deviceManagerComponent.setImplementation(OSGIDeviceManager.class);
        deviceManagerComponent.add(createServiceDependency().setService(ConfigurationManager.class).setRequired(true));
        deviceManagerComponent.add(createServiceDependency().setService(EventManager.class).setRequired(true));
        manager.add(deviceManagerComponent);

        // register variable manager
        variableManagerComponent = manager.createComponent();
        variableManagerComponent.setInterface(VariableManager.class.getName(), null);
        variableManagerComponent.setImplementation(OSGIVariableManager.class);
        variableManagerComponent.add(createServiceDependency().setService(EventManager.class).setRequired(true));
        manager.add(variableManagerComponent);

        // register trigger manager
        triggerManagerComponent = manager.createComponent();
        triggerManagerComponent.setInterface(TriggerManager.class.getName(), null);
        triggerManagerComponent.setImplementation(OSGITriggerManager.class);
        triggerManagerComponent.add(createServiceDependency().setService(EventManager.class).setRequired(true));
        manager.add(triggerManagerComponent);

        // register action manager
        actionManagerComponent = manager.createComponent();
        actionManagerComponent.setInterface(ActionManager.class.getName(), null);
        actionManagerComponent.setImplementation(OSGIActionManager.class);
        actionManagerComponent.add(createServiceDependency().setService(VariableManager.class).setRequired(true));
        actionManagerComponent.add(createServiceDependency().setService(EventManager.class).setRequired(true));
        actionManagerComponent.add(createServiceDependency().setService(ConfigurationManager.class).setRequired(true));
        actionManagerComponent.addStateListener(new ComponentStateListener() {
            @Override
            public void starting(Component component) {}

            @Override
            public void started(Component component) {
                publishDefaultActions((ActionManager)component.getService());
            }

            @Override
            public void stopping(Component component) {}

            @Override
            public void stopped(Component component) {}
        });
        manager.add(actionManagerComponent);

        // register disco manager
        discoManagerComponent = manager.createComponent();
        discoManagerComponent.setInterface(DiscoManager.class.getName(), null);
        discoManagerComponent.setImplementation(OSGIDiscoManager.class);
        manager.add(discoManagerComponent);
    }

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        manager.remove(triggerManagerComponent);
        manager.remove(deviceManagerComponent);
        manager.remove(variableManagerComponent);
        manager.remove(eventManagerComponent);
        manager.remove(configManagerComponent);
        manager.remove(discoManagerComponent);
    }

    private void publishDefaultActions(ActionManager manager) {
        String pluginId = FrameworkUtil.getBundle(getClass()).getSymbolicName();
        manager.publishAction(new EmailAction(pluginId));
        manager.publishAction(new LogAction(pluginId));
        manager.publishAction(new SendCommandToDeviceAction(pluginId));
    }
}
