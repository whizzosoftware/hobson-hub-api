/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

import java.util.Collection;

/**
 * A manager interface for device bridge discovery.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DiscoManager extends DeviceBridgeDetectionContext {
    /**
     * Publishes a new DeviceBridgeDetector.
     *
     * @param metaAnalyzer the analyzer to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDeviceBridgeDetector(String userId, String hubId, DeviceBridgeDetector metaAnalyzer);

    /**
     * Unpublishes a previously published ExternalBridgeMetaAnalyzer.
     *
     * @param detectorId the ID of the detector to unpublish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDeviceBridgeDetector(String userId, String hubId, String detectorId);

    /**
     * Returns a list of all discovered device bridges.
     *
     * @return a Collection of DeviceBridge instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<DeviceBridge> getDeviceBridges(String userId, String hubId);

    /**
     * Processes a DevceBridgeMetaData object. This will give all registered DeviceBridgeDetectors an
     * opportunity to attempt to identify the meta information.
     *
     * @param meta the object to process
     *
     * @since hobson-hub-api 0.1.6
     */
    public void processDeviceBridgeMetaData(String userId, String hubId, DeviceBridgeMetaData meta);
}
