/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger.manager;

import com.whizzosoftware.hobson.api.trigger.HobsonTrigger;
import com.whizzosoftware.hobson.api.trigger.TriggerException;
import com.whizzosoftware.hobson.api.trigger.TriggerProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An implementation of TriggerManager that publishes triggers as OSGi services.
 *
 * @author Dan Noguerol
 */
public class OSGITriggerManager implements TriggerManager {
    private volatile BundleContext bundleContext;

    public Collection<HobsonTrigger> getAllTriggers() {
        List<HobsonTrigger> triggers = new ArrayList<HobsonTrigger>();
        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(TriggerProvider.class.getName(), null);
            if (refs != null) {
                for (ServiceReference ref : refs) {
                    TriggerProvider provider = (TriggerProvider)bundleContext.getService(ref);
                    triggers.addAll(provider.getTriggers());
                }
            }
        } catch (InvalidSyntaxException e) {
            throw new TriggerException("Error obtaining trigger providers", e);
        }
        return triggers;
    }

    public HobsonTrigger getTrigger(String providerId, String triggerId) {
        try {
            Filter filter = bundleContext.createFilter("(&(objectClass=" + TriggerProvider.class.getName() + ")(providerId=" + providerId + "))");
            ServiceReference[] refs = bundleContext.getServiceReferences(TriggerProvider.class.getName(), filter.toString());
            if (refs != null && refs.length == 1) {
                TriggerProvider provider = (TriggerProvider)bundleContext.getService(refs[0]);
                return provider.getTrigger(triggerId);
            } else {
                throw new TriggerException("Unable to find unique trigger provider for id: " + providerId);
            }
        } catch (InvalidSyntaxException e) {
            throw new TriggerException("Error obtaining trigger providers", e);
        }
    }

    public void addTrigger(String providerId, Object trigger) {
        try {
            Filter filter = bundleContext.createFilter("(&(objectClass=" + TriggerProvider.class.getName() + ")(providerId=" + providerId + "))");
            ServiceReference[] refs = bundleContext.getServiceReferences(TriggerProvider.class.getName(), filter.toString());
            if (refs != null && refs.length == 1) {
                TriggerProvider provider = (TriggerProvider)bundleContext.getService(refs[0]);
                provider.addTrigger(trigger);
            } else {
                throw new TriggerException("Unable to find unique trigger provider for id: " + providerId);
            }
        } catch (InvalidSyntaxException e) {
            throw new TriggerException("Error obtaining trigger providers", e);
        }
    }

    @Override
    public void deleteTrigger(String providerId, String triggerId) {
        try {
            Filter filter = bundleContext.createFilter("(&(objectClass=" + TriggerProvider.class.getName() + ")(providerId=" + providerId + "))");
            ServiceReference[] refs = bundleContext.getServiceReferences(TriggerProvider.class.getName(), filter.toString());
            if (refs != null && refs.length == 1) {
                TriggerProvider provider = (TriggerProvider)bundleContext.getService(refs[0]);
                provider.deleteTrigger(triggerId);
            } else {
                throw new TriggerException("Unable to find unique trigger provider for id: " + providerId);
            }
        } catch (InvalidSyntaxException e) {
            throw new TriggerException("Error obtaining trigger providers", e);
        }
    }
}
