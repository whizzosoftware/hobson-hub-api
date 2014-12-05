/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.disco.DeviceAdvertisementListener;

/**
 * Event that occurs when a new DeviceAdvertisementListener is published.
 *
 * @author Dan Noguerol
 */
public class DeviceAdvertisementListenerPublishedEvent extends HobsonEvent {
    public static final String ID = "deviceAdvertisementListenerPublished";

    private DeviceAdvertisementListener listener;

    public DeviceAdvertisementListenerPublishedEvent(DeviceAdvertisementListener listener) {
        super(EventTopics.DISCO_TOPIC, ID);

        this.listener = listener;
    }

    public DeviceAdvertisementListener getListener() {
        return listener;
    }
}
