/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface for managing device telemetry.
 *
 * @author Dan Noguerol
 */
public interface TelemetryManager {
    /**
     * Indicates if this manager is a stub (NO-OP) implementation.
     *
     * @return a boolean
     */
    boolean isStub();

    /**
     * Creates a new data stream.
     *
     * @param ctx the hub context
     * @param name the stream name
     * @param data the variables that comprise the stream data
     *
     * @return the ID of the newly created data stream
     */
    String createDataStream(HubContext ctx, String name, Collection<VariableContext> data);

    /**
     * Returns the list of created data streams.
     *
     * @param ctx the hub context
     *
     * @return a Collection of DataStream instances
     */
    Collection<DataStream> getDataStreams(HubContext ctx);

    /**
     * Returns a specific data stream.
     *
     * @param ctx the hub context
     * @param dataStreamId the data stream ID
     *
     * @return a DataStream instance
     */
    DataStream getDataStream(HubContext ctx, String dataStreamId);

    /**
     * Returns a unique list of variables across all data streams.
     *
     * @param ctx the hub context
     *
     * @return a Collection of VariableContext instances
     */
    Set<VariableContext> getMonitoredVariables(HubContext ctx);

    /**
     * Add data point(s) to a data stream.
     *
     * @param ctx the hub context
     * @param streamName the stream name
     * @param now the time the data point occurred
     * @param data the data values
     */
    void addData(HubContext ctx, String streamName, long now, Map<VariableContext,Object> data);

    /**
     * Returns data from a data stream.
     *
     * @param ctx the hub context
     * @param streamName the stream name
     * @param endTime the end time desired
     * @param interval the interval size of the data
     *
     * @return a List of TemporalValue instances
     */
    List<TemporalValue> getData(HubContext ctx, String streamName, long endTime, TelemetryInterval interval);
}
