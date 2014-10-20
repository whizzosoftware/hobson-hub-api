/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.manager;

import com.whizzosoftware.hobson.api.event.VariableUpdateNotificationEvent;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.HobsonVariableImpl;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import org.osgi.framework.*;

import java.util.*;

/**
 * A VariableManager implementation that publishes variables as OSGi services and uses OSGi events to
 * change them.
 *
 * @author Dan Noguerol
 */
public class OSGIVariableManager implements VariableManager {
    private volatile EventManager eventManager;

    private static final String GLOBAL_NAME = "$GLOBAL$";

    private final Map<String,List<VariableRegistration>> variableRegistrations = new HashMap<>();

    @Override
    public void publishGlobalVariable(String pluginId, HobsonVariable var) {
        publishDeviceVariable(pluginId, GLOBAL_NAME, var);
    }

    @Override
    public void publishDeviceVariable(String pluginId, String deviceId, HobsonVariable var) {
        // make sure variable doesn't already exist
        if (hasDeviceVariable(pluginId, deviceId, var.getName())) {
            throw new HobsonRuntimeException("Attempt to publish a duplicate variable: " + pluginId + "," + deviceId + "," + var.getName());
        }

        // publish the variable
        Properties props = new Properties();
        props.setProperty("pluginId", pluginId);
        props.setProperty("deviceId", deviceId);
        props.setProperty("name", var.getName());
        addVariableRegistration(pluginId, deviceId, var.getName(), getBundleContext().registerService(
                HobsonVariable.class.getName(),
                var,
                props
        ));
    }

    @Override
    public void unpublishGlobalVariable(String pluginId, String name) {
        synchronized (variableRegistrations) {
            List<VariableRegistration> regs = variableRegistrations.get(pluginId);
            if (regs != null) {
                VariableRegistration vr = null;
                for (VariableRegistration reg : regs) {
                    if (reg.getPluginId().equals(pluginId) && reg.getName().equals(name)) {
                        vr = reg;
                        break;
                    }
                }
                if (vr != null) {
                    vr.unregister();
                    regs.remove(vr);
                }
            }
        }
    }

    @Override
    public void unpublishAllPluginVariables(String pluginId) {
        synchronized (variableRegistrations) {
            List<VariableRegistration> regs = variableRegistrations.get(pluginId);
            if (regs != null) {
                for (VariableRegistration vr : regs) {
                    vr.unregister();
                }
                variableRegistrations.remove(pluginId);
            }
        }
    }

    @Override
    public void unpublishDeviceVariable(String pluginId, String deviceId, String name) {
        synchronized (variableRegistrations) {
            List<VariableRegistration> regs = variableRegistrations.get(pluginId);
            if (regs != null) {
                VariableRegistration vr = null;
                for (VariableRegistration reg : regs) {
                    if (reg.getPluginId().equals(pluginId) && reg.getDeviceId().equals(deviceId) && reg.getName().equals(name)) {
                        vr = reg;
                        break;
                    }
                }
                if (vr != null) {
                    vr.unregister();
                    regs.remove(vr);
                }
            }
        }
    }

    @Override
    public void unpublishAllDeviceVariables(String pluginId, String deviceId) {
        synchronized (variableRegistrations) {
            List<VariableRegistration> regs = variableRegistrations.get(pluginId);
            if (regs != null) {
                VariableRegistration vr = null;
                for (VariableRegistration reg : regs) {
                    if (reg.getPluginId().equals(pluginId) && reg.getDeviceId().equals(deviceId)) {
                        vr = reg;
                        break;
                    }
                }
                if (vr != null) {
                    vr.unregister();
                    regs.remove(vr);
                }
            }
        }
    }

    @Override
    public Collection<HobsonVariable> getGlobalVariables() {
        List<HobsonVariable> results = new ArrayList<>();
        BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
        try {
            ServiceReference[] references = bundleContext.getServiceReferences(HobsonVariable.class.getName(), "(&(objectClass=" + HobsonVariable.class.getName() + ")(deviceId=" + GLOBAL_NAME + "))");
            if (references != null && references.length > 0) {
                for (ServiceReference ref : references) {
                    results.add((HobsonVariable)bundleContext.getService(ref));
                }
            }
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving global variables", e);
        }
        return results;
    }

    @Override
    public Collection<HobsonVariable> getDeviceVariables(String pluginId, String deviceId) {
        BundleContext bundleContext = getBundleContext();
        if (bundleContext != null) {
            try {
                ServiceReference[] refs = bundleContext.getServiceReferences(HobsonVariable.class.getName(), "(&(objectClass=" + HobsonVariable.class.getName() + ")(pluginId=" + pluginId + ")(deviceId=" + deviceId + "))");
                if (refs != null) {
                    List<HobsonVariable> results = new ArrayList<>();
                    for (ServiceReference ref : refs) {
                        results.add((HobsonVariable)bundleContext.getService(ref));
                    }
                    return results;
                }
            } catch (InvalidSyntaxException e) {
                throw new HobsonRuntimeException("Error looking up variables for device ID \"" + deviceId + "\"", e);
            }
        }
        return null;
    }

    @Override
    public HobsonVariable getDeviceVariable(String pluginId, String deviceId, String name) {
        BundleContext bundleContext = getBundleContext();
        if (bundleContext != null) {
            try {
                ServiceReference[] refs = bundleContext.getServiceReferences(null, "(&(objectClass=" +
                        HobsonVariable.class.getName() +
                        ")(pluginId=" +
                        pluginId +
                        ")(deviceId=" +
                        deviceId +
                        ")(name=" +
                        name +
                        "))"
                );
                if (refs != null && refs.length == 1) {
                    return (HobsonVariable) bundleContext.getService(refs[0]);
                } else if (refs != null && refs.length > 1) {
                    throw new HobsonRuntimeException("Found multiple variables for " + pluginId + "." + deviceId + "[" + name + "]");
                } else {
                    throw new VariableNotFoundException(pluginId, deviceId, name);
                }
            } catch (InvalidSyntaxException e) {
                throw new HobsonRuntimeException("Error looking up variable " + pluginId + "." + deviceId + "[" + name + "]", e);
            }
        }
        return null;
    }

    @Override
    public boolean hasDeviceVariable(String pluginId, String deviceId, String name) {
        BundleContext bundleContext = getBundleContext();
        if (bundleContext != null) {
            try {
                ServiceReference[] refs = bundleContext.getServiceReferences(null, "(&(objectClass=" +
                        HobsonVariable.class.getName() +
                        ")(pluginId=" +
                        pluginId +
                        ")(deviceId=" +
                        deviceId +
                        ")(name=" +
                        name +
                        "))"
                );
                if (refs != null && refs.length == 1) {
                    return true;
                } else if (refs != null && refs.length > 1) {
                    throw new HobsonRuntimeException("Found multiple variables for " + pluginId + "." + deviceId + "[" + name + "]");
                }
            } catch (InvalidSyntaxException e) {
                throw new HobsonRuntimeException("Error looking up variable " + pluginId + "." + deviceId + "[" + name + "]", e);
            }
        }
        return false;
    }

    @Override
    public Long setDeviceVariable(String pluginId, String deviceId, String name, Object value) {
        HobsonVariable variable = getDeviceVariable(pluginId, deviceId, name);
        Long lastUpdate = variable.getLastUpdate();
        eventManager.postEvent(new VariableUpdateRequestEvent(pluginId, deviceId, name, value));
        return lastUpdate;
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate update) {
        setVariable(update, true);
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        for (VariableUpdate update : updates) {
            setVariable(update, false);
        }
        eventManager.postEvent(new VariableUpdateNotificationEvent(updates));
    }

    protected void setVariable(VariableUpdate update, boolean generateEvent) {
        HobsonVariable var;

        // if the device ID is null, it's a global variable
        if (update.getDeviceId() == null) {
            var = getDeviceVariable(update.getPluginId(), GLOBAL_NAME, update.getName());
        } else {
            var = getDeviceVariable(update.getPluginId(), update.getDeviceId(), update.getName());
        }

        if (var != null) {
            ((HobsonVariableImpl)var).setValue(update.getValue());
        }
        if (generateEvent) {
            eventManager.postEvent(new VariableUpdateNotificationEvent(update));
        }
    }

    protected BundleContext getBundleContext() {
        Bundle bundle = FrameworkUtil.getBundle(getClass());
        if (bundle != null) {
            return bundle.getBundleContext();
        } else {
            return null;
        }
    }

    protected void addVariableRegistration(String pluginId, String deviceId, String name, ServiceRegistration reg) {
        synchronized (variableRegistrations) {
            List<VariableRegistration> regs = variableRegistrations.get(pluginId);
            if (regs == null) {
                regs = new ArrayList<>();
                variableRegistrations.put(pluginId, regs);
            }
            regs.add(new VariableRegistration(pluginId, deviceId, name, reg));
        }
    }

    private class VariableRegistration {
        private String pluginId;
        private String deviceId;
        private String name;
        private ServiceRegistration registration;

        public VariableRegistration(String pluginId, String deviceId, String name, ServiceRegistration registration) {
            this.pluginId = pluginId;
            this.deviceId = deviceId;
            this.name = name;
            this.registration = registration;
        }

        public String getPluginId() {
            return pluginId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getName() {
            return name;
        }

        public void unregister() {
            registration.unregister();
        }
    }
}