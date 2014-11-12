/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.manager;

import com.whizzosoftware.hobson.api.event.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * An EventManager implementation that uses the OSGi EventAdmin service.
 *
 * @author Dan Noguerol
 */
public class OSGIEventManager implements EventManager {
    volatile private BundleContext bundleContext;
    volatile private EventAdmin eventAdmin;

    private final Map<EventListener,ServiceRegistration> serviceRegMap = new HashMap<EventListener,ServiceRegistration>();

    @Override
    public void addListener(EventListener listener, Collection<String> topics) {
        Hashtable ht = new Hashtable();
        ht.put(EventConstants.EVENT_TOPIC, topics.toArray(new String[topics.size()]));
        synchronized (serviceRegMap) {
            if (serviceRegMap.containsKey(listener)) {
                serviceRegMap.get(listener).unregister();
            }
            serviceRegMap.put(
                    listener,
                    bundleContext.registerService(EventHandler.class.getName(), new EventHandlerAdapter(listener), ht)
            );
        }
    }

    @Override
    public void removeListener(EventListener listener) {
        synchronized (serviceRegMap) {
            ServiceRegistration reg = serviceRegMap.get(listener);
            if (reg != null) {
                reg.unregister();
                serviceRegMap.remove(listener);
            }
        }
    }

    @Override
    public void postEvent(HobsonEvent event) {
        eventAdmin.postEvent(event.getEvent());
    }

    private class EventHandlerAdapter implements EventHandler {
        private final Logger logger = LoggerFactory.getLogger(getClass());

        private EventListener listener;

        public EventHandlerAdapter(EventListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleEvent(Event event) {
            // TODO: find a more elegant way to perform OSGi -> Hobson event conversion
            logger.debug("Received event: {}", event);
            if (VariableUpdateNotificationEvent.ID.equals(HobsonEvent.readEventId(event))) {
                listener.onHobsonEvent(new VariableUpdateNotificationEvent(event));
            } else if (VariableUpdateRequestEvent.ID.equals(HobsonEvent.readEventId(event))) {
                listener.onHobsonEvent(new VariableUpdateRequestEvent(event));
            } else if (PresenceUpdateEvent.ID.equals(HobsonEvent.readEventId(event))) {
                listener.onHobsonEvent(new PresenceUpdateEvent(event));
            }
        }
    }
}
