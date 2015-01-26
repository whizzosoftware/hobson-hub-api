/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

/**
 * A DevicePublisher implementation that persists the devices it publishes and can re-publish them
 * when asked.
 *
 * @author Dan Noguerol
 */
public class PersistentDevicePublisher implements DevicePublisher {
    private DevicePublisher publisher;

    public PersistentDevicePublisher(DevicePublisher publisher) {
        this.publisher = publisher;
    }

    public void publishAll() {
        // TODO
    }

    @Override
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device) {
        publisher.publishDevice(plugin, device);
    }

    @Override
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device, boolean republish) {
        publisher.publishDevice(plugin, device, republish);
    }

    @Override
    public void unpublishDevice(HobsonPlugin plugin, String deviceId) {
        publisher.unpublishDevice(plugin, deviceId);
    }

    @Override
    public void unpublishAllDevices(HobsonPlugin plugin) {
        publisher.unpublishAllDevices(plugin);
    }
}
