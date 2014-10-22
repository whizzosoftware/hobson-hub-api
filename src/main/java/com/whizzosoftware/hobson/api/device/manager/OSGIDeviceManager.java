/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.manager;

import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.config.manager.DeviceConfigurationListener;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.event.DeviceStartedEvent;
import com.whizzosoftware.hobson.api.event.DeviceStoppedEvent;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.util.BundleUtil;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An implementation of DeviceManager that publishes Hobson devices as OSGi services.
 *
 * @author Dan Noguerol
 */
public class OSGIDeviceManager implements DeviceManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile ConfigurationManager configManager;
    private volatile EventManager eventManager;

    private final Map<String,List<DeviceServiceRegistration>> serviceRegistrations = new HashMap<String,List<DeviceServiceRegistration>>();

    public void setConfigManager(ConfigurationManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public Collection<HobsonDevice> getAllDevices() {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            List<HobsonDevice> results = new ArrayList<HobsonDevice>();
            ServiceReference[] references = context.getServiceReferences(HobsonDevice.class.getName(), null);
            if (references != null) {
                for (ServiceReference ref : references) {
                    results.add((HobsonDevice)context.getService(ref));
                }
            }
            return results;
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving devices", e);
        }
    }

    @Override
    public Collection<HobsonDevice> getAllPluginDevices(String pluginId) {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            List<HobsonDevice> results = new ArrayList<HobsonDevice>();
            ServiceReference[] references = context.getServiceReferences(null, "(&(objectClass=" + HobsonDevice.class.getName() + ")(pluginId=" + pluginId + "))");
            if (references != null) {
                for (ServiceReference ref : references) {
                    results.add((HobsonDevice)context.getService(ref));
                }
            }
            return results;
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving device", e);
        }
    }

    @Override
    public HobsonDevice getDevice(String pluginId, String deviceId) {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            ServiceReference[] references = context.getServiceReferences(null, "(&(objectClass=" + HobsonDevice.class.getName() + ")(pluginId=" + pluginId + ")(deviceId=" + deviceId + "))");
            if (references != null && references.length == 1) {
                return (HobsonDevice)context.getService(references[0]);
            } else if (references != null && references.length > 1) {
                throw new HobsonRuntimeException("Duplicate devices detected");
            } else {
                throw new DeviceNotFoundException(pluginId, deviceId);
            }
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving device", e);
        }
    }

    @Override
    public boolean hasDevice(String pluginId, String deviceId) {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            ServiceReference[] references = context.getServiceReferences(null, "(&(objectClass=" + HobsonDevice.class.getName() + ")(pluginId=" + pluginId + ")(deviceId=" + deviceId + "))");
            if (references != null && references.length == 1) {
                return true;
            } else if (references != null && references.length > 1) {
                throw new HobsonRuntimeException("Duplicate devices detected");
            }
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving device", e);
        }
        return false;
    }

    @Override
    synchronized public void publishDevice(final HobsonPlugin plugin, final HobsonDevice device) {
        BundleContext context = BundleUtil.getBundleContext(getClass(), device.getPluginId());

        // check that the device doesn't already exist
        if (hasDevice(device.getPluginId(), device.getId())) {
            throw new HobsonRuntimeException("Attempt to publish a duplicate device: " + device.getId() + ", " + device.getId());
        }

        if (context != null) {
            // register device as a service
            Properties props = new Properties();
            props.setProperty("pluginId", device.getPluginId());
            props.setProperty("deviceId", device.getId());
            ServiceRegistration deviceReg = context.registerService(
                    HobsonDevice.class.getName(),
                    device,
                    props
            );
            addServiceRegistration(device.getPluginId(), deviceReg);

            // register to monitor device configuration (it's important to do this AFTER device service registration)
            configManager.registerForDeviceConfigurationUpdates(
                    device.getPluginId(),
                    device.getId(),
                    new DeviceConfigurationListener() {
                        @Override
                        public void onDeviceConfigurationUpdate(final String deviceId, final Dictionary config) {
                            plugin.executeInEventLoop(new Runnable() {
                                @Override
                                public void run() {
                                    plugin.onDeviceConfigurationUpdate(deviceId, config);
                                }
                            });
                        }
                    }
            );

            logger.debug("Device {} registered", device.getId());

            // execute the device's startup method using its plugin event loop
            plugin.executeInEventLoop(new Runnable() {
                @Override
                public void run() {
                    device.onStartup();
                    eventManager.postEvent(new DeviceStartedEvent(device));
                }
            });
        }
    }

    @Override
    synchronized public void unpublishAllDevices(HobsonPlugin plugin) {
        List<DeviceServiceRegistration> regs = serviceRegistrations.get(plugin.getId());
        if (regs != null) {
            try {
                for (DeviceServiceRegistration reg : regs) {
                    try {
                        final HobsonDevice device = reg.getDevice();
                        reg.unregister();
                        // execute the device's shutdown method using its plugin event loop
                        plugin.executeInEventLoop(new Runnable() {
                            @Override
                            public void run() {
                                device.onShutdown();
                                eventManager.postEvent(new DeviceStoppedEvent(device));
                            }
                        });
                    } catch (Exception e) {
                        logger.error("Error stopping device for " + plugin.getId(), e);
                    }
                }
            } finally {
                serviceRegistrations.remove(plugin.getId());
            }
        }
    }

    @Override
    synchronized public void unpublishDevice(HobsonPlugin plugin, String deviceId) {
        List<DeviceServiceRegistration> regs = serviceRegistrations.get(plugin.getId());
        if (regs != null) {
            DeviceServiceRegistration dsr = null;
            try {
                for (final DeviceServiceRegistration reg : regs) {
                    final HobsonDevice device = reg.getDevice();
                    if (device != null && device.getId().equals(deviceId)) {
                        dsr = reg;
                        // execute the device's shutdown method using its plugin event loop
                        plugin.executeInEventLoop(new Runnable() {
                            @Override
                            public void run() {
                                device.onShutdown();
                                eventManager.postEvent(new DeviceStoppedEvent(reg.getDevice()));
                            }
                        });
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("Error stopping device " + plugin.getId() + "." + deviceId, e);
            } finally {
                if (dsr != null) {
                    dsr.unregister();
                    regs.remove(dsr);
                }
            }
        }
    }

    @Override
    public void setDeviceName(String pluginId, String deviceId, String name) {
        configManager.setDeviceConfigurationProperty(pluginId, deviceId, "name", name, true);
    }

    synchronized private void addServiceRegistration(String pluginId, ServiceRegistration deviceRegistration) {
        List<DeviceServiceRegistration> regs = serviceRegistrations.get(pluginId);
        if (regs == null) {
            regs = new ArrayList<>();
            serviceRegistrations.put(pluginId, regs);
        }
        regs.add(new DeviceServiceRegistration(deviceRegistration));
    }

    private class DeviceServiceRegistration {
        private ServiceRegistration deviceRegistration;

        public DeviceServiceRegistration(ServiceRegistration deviceRegistration) {
            this.deviceRegistration = deviceRegistration;
        }

        public HobsonDevice getDevice() {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            ServiceReference ref = deviceRegistration.getReference();
            return (HobsonDevice)context.getService(ref);
        }

        public void unregister() {
            deviceRegistration.unregister();
        }
    }
}
