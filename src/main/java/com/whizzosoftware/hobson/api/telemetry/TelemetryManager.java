/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;
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
     * @param userId the user ID
     * @param name the stream name
     * @param variables the variables that comprise the stream data
     *
     * @return the ID of the newly created data stream
     */
    String createDataStream(String userId, String name, Collection<VariableContext> variables);

    /**
     * Returns the list of created data streams.
     *
     * @param userId the user ID
     *
     * @return a Collection of DataStream instances
     */
    Collection<DataStream> getDataStreams(String userId);

    /**
     * Returns a specific data stream.
     *
     * @param userId the user ID
     * @param dataStreamId the data stream ID
     *
     * @return a DataStream instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if the data stream does not exist
     */
    DataStream getDataStream(String userId, String dataStreamId);

    /**
     * Returns a unique list of variables across all data streams.
     *
     * @param userId the user ID
     *
     * @return a Collection of VariableContext instances
     */
    Set<VariableContext> getMonitoredVariables(String userId);

    /**
     * Add data point(s) to a data stream.
     *
     * @param userId the user ID
     * @param dataStreamId the data stream ID
     * @param now the time the data point occurred
     * @param data the data values
     */
    void addData(String userId, String dataStreamId, long now, Map<VariableContext,Object> data);

    /**
     * Returns data from a data stream.
     *
     * @param userId the user ID
     * @param dataStreamId the data stream ID
     * @param endTime the end time desired
     * @param interval the interval size of the data
     *
     * @return a Collection of TemporalValue instances
     */
    Collection<TemporalValueSet> getData(String userId, String dataStreamId, long endTime, TelemetryInterval interval);
}
