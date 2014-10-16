/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco.manager;

import com.whizzosoftware.hobson.api.disco.*;

import java.util.Collection;

/**
 * A manager interface for entity discovery. This is currently just for external bridges but could accomodate discovery
 * of other entities as well.
 *
 * @author Dan Noguerol
 */
public interface DiscoManager extends ExternalBridgeMetaAnalysisContext {
    /**
     * Publishes a new ExternalBridgeMetaAnalyzer.
     *
     * @param metaAnalyzer the analyzer to publish
     */
    public void publishExternalBridgeMetaAnalyzer(ExternalBridgeMetaAnalyzer metaAnalyzer);

    /**
     * Unpublishes a previously published ExternalBridgeMetaAnalyzer.
     *
     * @param metaAnalyzerId the ID of the analyzer to unpublish
     */
    public void unpublishExternalBridgeMetaAnalyzer(String metaAnalyzerId);

    /**
     * Returns a list of all discovered external bridges.
     *
     * @return a Collection of ExternalBridge instances
     */
    public Collection<ExternalBridge> getExternalBridges();

    /**
     * Processes an ExternalBridgeMeta object. This will give all registered ExternalBridgeMetaAnalyzers an
     * opportunity to attempt to identify the meta information.
     *
     * @param meta the object to process
     */
    public void processExternalBridgeMeta(ExternalBridgeMeta meta);
}
