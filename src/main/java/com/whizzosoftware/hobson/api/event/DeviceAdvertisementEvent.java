/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;

import java.util.Map;

/**
 * An event that is published when a device advertisement is detected.
 *
 * @author Dan Noguerol
 */
public class DeviceAdvertisementEvent extends HobsonEvent {
    public static final String ID = "deviceAdvertisement";
    public static final String PROP_ADVERTISEMENT = "advertisement";

    public DeviceAdvertisementEvent(DeviceAdvertisement advertisement) {
        super(EventTopics.createDiscoTopic(advertisement.getProtocolId()), ID);
        setProperty(PROP_ADVERTISEMENT, advertisement);
    }

    public DeviceAdvertisementEvent(Map<String,Object> props) {
        super(EventTopics.createDiscoTopic(((DeviceAdvertisement)props.get(PROP_ADVERTISEMENT)).getProtocolId()), props);
    }

    public DeviceAdvertisement getAdvertisement() {
        return (DeviceAdvertisement)getProperty(PROP_ADVERTISEMENT);
    }
}
