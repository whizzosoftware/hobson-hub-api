/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config.manager;

import com.whizzosoftware.hobson.api.config.ConfigurationException;
import com.whizzosoftware.hobson.bootstrap.api.util.BundleUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;

import java.io.IOException;
import java.util.*;

/**
 * An implementation of ConfigManager that uses the OSGi ConfigurationAdmin service.
 *
 * @author Dan Noguerol
 */
public class OSGIConfigurationManager implements ConfigurationManager {
    volatile private BundleContext bundleContext;
    volatile private ConfigurationAdmin configAdmin;

    private final static String DEVICE_PID_SEPARATOR = ".";

    private final Map<String,ServiceRegistration> managedServiceRegistrations = new HashMap<String,ServiceRegistration>();

    @Override
    public Dictionary getPluginConfiguration(String pluginId) {
        if (bundleContext != null) {
            try {
                Bundle bundle = BundleUtil.getBundleForSymbolicName(pluginId);
                if (bundle != null) {
                    Configuration config = configAdmin.getConfiguration(pluginId, bundle.getLocation());
                    if (config != null) {
                        Dictionary properties = config.getProperties();
                        if (properties != null) {
                            return properties;
                        }
                    }
                }
            } catch (IOException e) {
                throw new ConfigurationException("Unable to obtain configuration for " + pluginId, e);
            }
        }
        throw new ConfigurationException("Unable to obtain configuration for " + pluginId);
    }

    @Override
    public void setPluginConfigurationProperty(String pluginId, String name, Object value) {
        try {
            Bundle bundle = BundleUtil.getBundleForSymbolicName(pluginId);
            if (bundle != null) {
                if (configAdmin != null) {
                    Configuration config = configAdmin.getConfiguration(pluginId, bundle.getLocation());
                    Dictionary dic = config.getProperties();
                    if (dic == null) {
                        dic = new Hashtable();
                    }
                    dic.put(name, value);
                    config.update(dic);
                } else {
                    throw new ConfigurationException("Unable to set device name: ConfigurationAdmin service is not available");
                }
            } else {
                throw new ConfigurationException("Unable to set configuration property: no bundle found");
            }
        } catch (IOException e) {
            throw new ConfigurationException("Error obtaining configuration", e);
        }
    }

    @Override
    public Dictionary getDeviceConfiguration(String pluginId, String deviceId) {
        try {
            if (configAdmin != null) {
                Bundle bundle = BundleUtil.getBundleForSymbolicName(pluginId);
                if (bundle != null) {
                    Configuration config = configAdmin.getConfiguration(pluginId + "." + deviceId, bundle.getLocation());
                    return config.getProperties();
                } else {
                    throw new ConfigurationException("Unable to get bundle for " + pluginId);
                }
            } else {
                throw new ConfigurationException("Unable to get device configuration; ConfigurationAdmin service is not available");
            }
        } catch (IOException e) {
            throw new ConfigurationException("Error obtaining configuration", e);
        }
    }

    @Override
    public void setDeviceConfigurationProperty(String pluginId, String deviceId, String name, Object value, boolean overwrite) {
        try {
            for (Bundle bundle : bundleContext.getBundles()) {
                if (pluginId.equals(bundle.getSymbolicName())) {
                    if (configAdmin != null) {
                        Configuration config = configAdmin.getConfiguration(pluginId + "." + deviceId, bundle.getLocation());
                        Dictionary dic = config.getProperties();
                        if (dic == null) {
                            dic = new Hashtable();
                        }
                        if (dic.get(name) == null || overwrite) {
                            dic.put(name, value);
                            config.update(dic);
                        }
                        return;
                    } else {
                        throw new ConfigurationException("Unable to set device name: ConfigurationAdmin service is not available");
                    }
                }
            }
        } catch (IOException e) {
            throw new ConfigurationException("Error obtaining configuration", e);
        }
    }

    @Override
    public void registerForPluginConfigurationUpdates(String pluginId, final PluginConfigurationListener listener) {
        synchronized (managedServiceRegistrations) {
            Dictionary props = new Hashtable();
            props.put("service.pid", pluginId);
            BundleContext context = BundleUtil.getBundleForSymbolicName(pluginId).getBundleContext();
            managedServiceRegistrations.put(
                pluginId,
                context.registerService(ManagedService.class.getName(), new ManagedService() {
                    @Override
                    public void updated(Dictionary config) throws org.osgi.service.cm.ConfigurationException {
                        if (config == null) {
                            config = new Hashtable();
                        }
                        listener.onPluginConfigurationUpdate(config);
                    }
                }, props)
            );
        }
    }

    @Override
    public void registerForDeviceConfigurationUpdates(String pluginId, final String deviceId, final DeviceConfigurationListener listener) {
        synchronized (managedServiceRegistrations) {
            String devicePID = getDevicePID(pluginId, deviceId);

            Dictionary dic = new Hashtable();
            dic.put("service.pid", devicePID);
            dic.put("pluginId", pluginId);
            dic.put("deviceId", deviceId);
            BundleContext context = BundleUtil.getBundleForSymbolicName(pluginId).getBundleContext();
            managedServiceRegistrations.put(
                devicePID,
                context.registerService(ManagedService.class.getName(), new ManagedService() {
                    @Override
                    public void updated(Dictionary config) throws org.osgi.service.cm.ConfigurationException {
                        if (config == null) {
                            config = new Hashtable();
                        }
                        listener.onDeviceConfigurationUpdate(deviceId, config);
                    }
                }, dic)
            );
        }
    }

    @Override
    public void unregisterForConfigurationUpdates(String pluginId, PluginConfigurationListener listener) {
        synchronized (managedServiceRegistrations) {
            // build a list of keys that need to be unregistered and removed
            List<String> removalSet = new ArrayList<String>();
            String pluginDevicePrefix = pluginId + DEVICE_PID_SEPARATOR;
            for (String key : managedServiceRegistrations.keySet()) {
                if (key.equals(pluginId) || key.startsWith(pluginDevicePrefix)) {
                    removalSet.add(key);
                }
            }

            // unregister and remove keys
            for (String key : removalSet) {
                ServiceRegistration reg = managedServiceRegistrations.get(key);
                reg.unregister();
                managedServiceRegistrations.remove(key);
            }
        }
    }

    private String getDevicePID(String pluginId, String deviceId) {
        return pluginId + DEVICE_PID_SEPARATOR + deviceId;
    }
}
