/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.manager;

import com.whizzosoftware.hobson.api.action.HobsonAction;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.util.BundleUtil;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An ActionManager implementation that publishes Hobson actions as OSGi services.
 *
 * @author Dan Noguerol
 */
public class OSGIActionManager implements ActionManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile BundleContext bundleContext;
    private volatile VariableManager variableManager;
    private volatile EventManager eventManager;
    private volatile ConfigurationManager configManager;

    @Override
    public void publishAction(HobsonAction action) {
        BundleContext context = BundleUtil.getBundleContext(getClass(), action.getPluginId());

        if (context != null) {
            // register device as a service
            Properties props = new Properties();
            if (action.getPluginId() == null) {
                logger.error("Unable to publish action with null plugin ID");
            } else if (action.getId() == null) {
                logger.error("Unable to publish action with null ID");
            } else {
                props.setProperty("pluginId", action.getPluginId());
                props.setProperty("actionId", action.getId());
                context.registerService(
                        HobsonAction.class.getName(),
                        action,
                        props
                );

                // set the action's managers
                action.setVariableManager(variableManager);
                action.setConfigurationManager(configManager);

                logger.debug("Action {} published", action.getId());
            }
        } else {
            throw new HobsonRuntimeException("Unable to obtain context to publish action");
        }
    }

    @Override
    public void executeAction(String pluginId, String actionId, Map<String, Object> properties) {
        try {
            Filter filter = bundleContext.createFilter("(&(objectClass=" + HobsonAction.class.getName() + ")(pluginId=" + pluginId + ")(actionId=" + actionId + "))");
            ServiceReference[] refs = bundleContext.getServiceReferences(HobsonAction.class.getName(), filter.toString());
            if (refs != null && refs.length == 1) {
                HobsonAction action = (HobsonAction)bundleContext.getService(refs[0]);
                action.execute(properties);
            } else {
                throw new HobsonRuntimeException("Unable to find action with ID: " + actionId);
            }
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error executing action with ID: " + actionId, e);
        }
    }

    @Override
    public Collection<HobsonAction> getAllActions() {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            List<HobsonAction> results = new ArrayList<HobsonAction>();
            ServiceReference[] references = context.getServiceReferences(HobsonAction.class.getName(), null);
            if (references != null) {
                for (ServiceReference ref : references) {
                    results.add((HobsonAction)context.getService(ref));
                }
            }
            return results;
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving actions", e);
        }
    }

    @Override
    public HobsonAction getAction(String pluginId, String actionId) {
        try {
            BundleContext context = BundleUtil.getBundleContext(getClass(), null);
            Filter filter = context.createFilter("(&(objectClass=" + HobsonAction.class.getName() + ")(pluginId=" + pluginId + ")(actionId=" + actionId + "))");
            ServiceReference[] references = context.getServiceReferences(null, filter.toString());
            if (references != null && references.length > 0) {
                return (HobsonAction)context.getService(references[0]);
            }
            return null;
        } catch (InvalidSyntaxException e) {
            throw new HobsonRuntimeException("Error retrieving action", e);
        }
    }
}
