/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A manager interface for managing device advertisement listeners.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DiscoManager {
    /**
     * Publishes a new DeviceAdvertisementListener.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param protocolId the protocol that the listener is interested in receiving advertisements for
     * @param listener the listener to publish
     *
     * @since hobson-hub-api 0.1.8
     */
    public void publishDeviceAdvertisementListener(String userId, String hubId, String protocolId, DeviceAdvertisementListener listener);

    /**
     * Unpublishes a previously published DeviceAdvertisementListener.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param listener the listener to remove
     *
     * @since hobson-hub-api 0.1.8
     */
    public void unpublishDeviceAdvertisementListener(String userId, String hubId, DeviceAdvertisementListener listener);

    /**
     * Fires a DeviceAdvertisement. This should perform callbacks for any registered DeviceAdvertisementListeners
     * that are available for the advertisement's protocol.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param advertisement the advertisement to publish
     */
    public void fireDeviceAdvertisement(String userId, String hubId, DeviceAdvertisement advertisement);
}
