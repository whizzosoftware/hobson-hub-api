/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A class that creates DeviceBridge objects. The detector will look at DeviceBridgeMetaData created by a
 * DeviceBridgeScanner to determine if it is something it recognizes. If so, it creates the corresponding
 * DeviceBridge object.
 *
 * @author Dan Noguerol
 */
public interface DeviceBridgeDetector {
    /**
     * Returns the plugin ID of this analyzer.
     *
     * @return a plugin ID
     */
    public String getPluginId();

    /**
     * Returns the ID of this analyzer.
     *
     * @return an ID
     */
    public String getId();

    /**
     * Attempt to identify an ExternalBridgeMeta object.
     *
     * @param context the analysis context
     * @param meta the meta object to analyze
     *
     * @return a boolean indicating whether the meta object was identified or not
     */
    public boolean identify(DeviceBridgeDetectionContext context, DeviceBridgeMetaData meta);
}
