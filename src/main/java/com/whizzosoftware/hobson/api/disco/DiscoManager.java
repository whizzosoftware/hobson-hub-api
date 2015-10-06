/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Collection;

/**
 * A manager interface for managing device advertisement listeners.
 *
 * Note that this interface differentiates between "internal" and "external" advertisements. An internal advertisement
 * is one that is published by a plugin and will be used when servicing external search requests -- they make internal
 * Hub services discoverable. An external advertisement is one that was made by an external (to the Hub) entity -- they
 * allow external entities to be discovered by Hub plugins.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DiscoManager {
    /**
     * Allows a plugin to request all known external device advertisements. This is an asynchronous call that will be
     * serviced as multiple DeviceAdvertisementEvent events to the plugin's onHobsonEvent() callback.
     *
     * @param ctx the context of the plugin requesting the snapshot
     * @param protocolId the protocol ID for the advertisements requested
     */
    void requestExternalDeviceAdvertisementSnapshot(PluginContext ctx, String protocolId);

    /**
     * Returns all published internal device advertisements.
     * @param ctx the hub context
     * @param protocolId the protocol associated with the advertisement
     *
     * @return a Collection of DeviceAdvertisement objects (or null if none are found)
     */
    Collection<DeviceAdvertisement> getInternalDeviceAdvertisements(HubContext ctx, String protocolId);

    /**
     * Returns a specific published internal device advertisement.
     *
     * @param ctx the hub context
     * @param protocolId the protocol associated with the advertisement
     * @param advId a advertisement ID
     *
     * @return a DeviceAdvertisement instance (or null if the protocol/ID combination was not found)
     */
    DeviceAdvertisement getInternalDeviceAdvertisement(HubContext ctx, String protocolId, String advId);

    /**
     * Publishes an internal DeviceAdvertisement. This will insure that the advertisement is included in any discovery
     * requests that external clients make for a particular protocol.
     *
     * @param ctx the context of the hub publishing the advertisement
     * @param advertisement the advertisement to publish
     */
    void publishDeviceAdvertisement(HubContext ctx, DeviceAdvertisement advertisement, boolean internal);
}
