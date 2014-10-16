/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco.manager;

import com.whizzosoftware.hobson.api.disco.*;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An OSGi-based implementation of a DiscoManager.
 *
 * Note: This is currently a very naive, brute-force implementation of the mechanism and doesn't have any
 * optimizations yet.
 *
 * @author Dan Noguerol
 */
public class OSGIDiscoManager implements DiscoManager, ExternalBridgeMetaAnalysisContext {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile BundleContext bundleContext;

    private final Map<String,ExternalBridge> bridgeMap = new HashMap<String,ExternalBridge>();
    private final Map<String,ServiceRegistration> analyzerRegistrationMap = new HashMap<String,ServiceRegistration>();

    public void start() {
        logger.debug("OSGIDiscoManager starting");
    }

    public void stop() {
        logger.debug("OSGIDiscoManager stopping");

        synchronized (analyzerRegistrationMap) {
            for (ServiceRegistration reg : analyzerRegistrationMap.values()) {
                reg.unregister();
            }
            analyzerRegistrationMap.clear();
        }
    }

    @Override
    public Collection<ExternalBridge> getExternalBridges() {
        ArrayList<ExternalBridge> results = new ArrayList<ExternalBridge>();
        synchronized (bridgeMap) {
            for (ExternalBridge disco : bridgeMap.values()) {
                results.add(disco);
            }
        }
        return results;
    }

    @Override
    public void publishExternalBridgeMetaAnalyzer(ExternalBridgeMetaAnalyzer metaAnalyzer) {
        logger.trace("Adding DiscoAnalyzer: {}", metaAnalyzer.getId());
        synchronized (analyzerRegistrationMap) {
            Dictionary dic = new Hashtable();
            dic.put("id", metaAnalyzer.getId());
            dic.put("pluginId", metaAnalyzer.getPluginId());
            analyzerRegistrationMap.put(
                    metaAnalyzer.getId(),
                    bundleContext.registerService(ExternalBridgeMetaAnalyzer.class.getName(), metaAnalyzer, dic)
            );
            refreshScanners();
        }
    }

    @Override
    public void unpublishExternalBridgeMetaAnalyzer(String metaAnalyzerId) {
        logger.trace("Removing DiscoAnalyzer: {}", metaAnalyzerId);
        synchronized (analyzerRegistrationMap) {
            ServiceRegistration reg = analyzerRegistrationMap.get(metaAnalyzerId);
            if (reg != null) {
                reg.unregister();
                analyzerRegistrationMap.remove(metaAnalyzerId);
            }
        }
    }

    @Override
    public void processExternalBridgeMeta(ExternalBridgeMeta meta) {
        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(ExternalBridgeMetaAnalyzer.class.getName(), null);
            if (refs != null) {
                for (ServiceReference ref : refs) {
                    ExternalBridgeMetaAnalyzer di = (ExternalBridgeMetaAnalyzer)bundleContext.getService(ref);
                    if (di.identify(this, meta)) {
                        break;
                    }
                }
            }
        } catch (InvalidSyntaxException e) {
            logger.error("Error attempting to perform discovery", e);
        }
    }

    @Override
    public void addExternalBridge(ExternalBridge bridge) {
        synchronized (bridgeMap) {
            logger.debug("Added identified entity: {} ({})", bridge.getValue(), bridge.getName());
            bridgeMap.put(bridge.getValue(), bridge);
        }
    }

    @Override
    public void removeExternalBridge(String bridgeId) {
        synchronized (bridgeMap) {
            logger.debug("Removed identified entity: {}", bridgeId);
            bridgeMap.remove(bridgeId);
        }
    }

    private void refreshScanners() {
        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(ExternalBridgeScanner.class.getName(), null);
            if (refs != null) {
                for (ServiceReference ref : refs) {
                    ExternalBridgeScanner scanner = (ExternalBridgeScanner)bundleContext.getService(ref);
                    scanner.refresh();
                }
            }
        } catch (InvalidSyntaxException e) {
            logger.error("An error occurred refreshing ExternalBridgeScanners", e);
        }
    }
}
