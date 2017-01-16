/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.data;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An interface for managing data streams.
 *
 * @author Dan Noguerol
 */
public interface DataStreamManager {
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
     * @param fields the fields that comprise the data stream
     * @param tags the tags associated with the data stream
     *
     * @return the ID of the newly created data stream
     */
    String createDataStream(HubContext ctx, String name, Collection<DataStreamField> fields, Set<String> tags);

    /**
     * Deletes an existing data stream.
     *
     * @param ctx the hub context
     * @param dataStreamId the ID of the stream to delete
     */
    void deleteDataStream(HubContext ctx, String dataStreamId);

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
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if the data stream does not exist
     */
    DataStream getDataStream(HubContext ctx, String dataStreamId);

    /**
     * Adds data to a data stream.
     *
     * @param ctx the hub context
     * @param dataStreamId the data stream ID
     * @param now the time the data point occurred
     * @param data the data values (a map of fieldId to value)
     */
    void addData(HubContext ctx, String dataStreamId, long now, Map<String, Object> data);

    /**
     * Returns data from a data stream.
     *
     * @param ctx the hub context
     * @param dataStreamId the data stream ID
     * @param endTime the end time desired
     * @param interval the interval size of the data
     *
     * @return a Collection of TemporalValue instances
     */
    Collection<DataStreamValueSet> getData(HubContext ctx, String dataStreamId, long endTime, DataStreamInterval interval);
}
