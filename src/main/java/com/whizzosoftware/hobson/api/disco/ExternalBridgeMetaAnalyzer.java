/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A class that can convert ExternalBridgeMeta objects into ExternalBridge objects. The analyzer will look at the
 * meta data to determine if it is something identifiable and, if so, create the corresponding ExternalBridge object.
 *
 * @author Dan Noguerol
 */
public interface ExternalBridgeMetaAnalyzer {
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
    public boolean identify(ExternalBridgeMetaAnalysisContext context, ExternalBridgeMeta meta);
}
