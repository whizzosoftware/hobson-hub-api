/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;
import java.util.Map;

/**
 * An interface for storage of data stream data.
 *
 * @author Dan Noguerol
 */
public interface DataStreamDataStore {
    /**
     * Add data to a data stream.
     *
     * @param userId the user ID that owns the stream
     * @param dataStreamId the data stream ID
     * @param timestamp the timestamp of the data
     * @param data the data values
     */
    void addData(String userId, String dataStreamId, long timestamp, Map<VariableContext, Object> data);

    /**
     * Retrieves data values from a data stream.
     *
     * @param userId the user ID that owns the stream
     * @param dataStreamId the data stream ID
     * @param endTime the end time of the desired data interval
     * @param interval the desired data interval
     *
     * @return a Collection of TemporalValueSet objects (returns an empty collection if there's no data)
     */
    Collection<TemporalValueSet> getData(String userId, String dataStreamId, long endTime, TelemetryInterval interval);
}