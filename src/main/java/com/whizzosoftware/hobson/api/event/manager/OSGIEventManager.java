/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.manager;

import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.event.VariableUpdateNotificationEvent;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Map<EventManagerListener,ServiceRegistration> serviceRegMap = new HashMap<EventManagerListener,ServiceRegistration>();

    @Override
    public void addListener(EventManagerListener listener, String topic) {
        String[] topics = new String[] {topic};
        Hashtable ht = new Hashtable();
        ht.put(EventConstants.EVENT_TOPIC, topics);
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
    public void removeListener(EventManagerListener listener) {
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

        private EventManagerListener listener;

        public EventHandlerAdapter(EventManagerListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleEvent(Event event) {
            logger.debug("Received event: {}", event);
            if (VariableUpdateNotificationEvent.ID.equals(HobsonEvent.readEventId(event))) {
                listener.onHobsonEvent(new VariableUpdateNotificationEvent(event));
            } else if (VariableUpdateRequestEvent.ID.equals(HobsonEvent.readEventId(event))) {
                listener.onHobsonEvent(new VariableUpdateRequestEvent(event));
            }
        }
    }
}
