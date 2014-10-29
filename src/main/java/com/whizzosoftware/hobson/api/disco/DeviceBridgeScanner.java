/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * Interface for classes that can scan for device bridges.
 *
 * @author Dan Noguerol
 */
public interface DeviceBridgeScanner {
    /**
     * Returns the plugin ID that owns this scanner.
     *
     * @return a plugin ID
     */
    public String getPluginId();

    /**
     * Returns the ID of the scanner.
     *
     * @return an ID
     */
    public String getId();

    /**
     * Starts the scanner.
     */
    public void start();

    /**
     * Stops the scanner.
     */
    public void stop();

    /**
     * Causes the scanner to refresh. This is called when new analyzers are registered so the scanners can make
     * their best effort to re-publish any DeviceBridgeMetaData objects that have already been found.
     */
    public void refresh();
}
