/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.ConfigurationException;
import org.osgi.framework.*;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * A context object that allows a device to both retrieve and save changes to its configuration.
 *
 * @author Dan Noguerol
 */
public class DeviceConfigurationContext {
    private String bundleSymbolicName;
    private String pid;
    private Dictionary configuration;

    public DeviceConfigurationContext(String bundleSymbolicName, String pid) {
        this.bundleSymbolicName = bundleSymbolicName;
        this.pid = pid;
        setConfiguration(getConfiguration().getProperties());
    }

    public DeviceConfigurationContext(String bundleSymbolicName, String pid, Dictionary configuration) {
        this.bundleSymbolicName = bundleSymbolicName;
        this.pid = pid;
        setConfiguration(configuration);
    }

    public void setProperty(String name, Object value) {
        setProperty(name, value, true);
    }

    public void setProperty(String name, Object value, boolean overwrite) {
        Object v = configuration.get(name);
        if (v == null || overwrite) {
            configuration.put(name, value);
        }
    }

    public boolean hasProperty(String name) {
        return (configuration.get(name) != null);
    }

    public Object getProperty(String name) {
        return configuration.get(name);
    }

    public void save() {
        try {
            getConfiguration().update(this.configuration);
        } catch (IOException e) {
            throw new ConfigurationException("Error saving configuration", e);
        }
    }

    private void setConfiguration(Dictionary configuration) {
        this.configuration = configuration;
        if (this.configuration == null) {
            this.configuration = new Hashtable();
        }
    }

    private Configuration getConfiguration() {
        try {
            BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();

            String bundleLocation = null;
            for (Bundle bundle : context.getBundles()) {
                if (bundleSymbolicName.equals(bundle.getSymbolicName())) {
                    bundleLocation = bundle.getLocation();
                }
            }

            if (bundleLocation != null) {
                ServiceReference[] refs = context.getServiceReferences(ConfigurationAdmin.class.getName(), null);
                if (refs != null && refs.length == 1) {
                    ConfigurationAdmin ca = (ConfigurationAdmin) context.getService(refs[0]);
                    return ca.getConfiguration(pid, bundleLocation);
                } else {
                    throw new ConfigurationException("Unable to obtain reference to ConfigurationAdmin");
                }
            } else {
                throw new ConfigurationException("Unable to determine bundle location for " + bundleSymbolicName);
            }
        } catch (Exception e) {
            throw new ConfigurationException("Error obtaining configuration", e);
        }
    }
}
