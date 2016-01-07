/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.activity;

import java.util.List;

/**
 * An interface for classes that manage the activity log. The activity log records interesting events that occur on the
 * Hub (such as executing tasks and device state changes) for reporting purposes.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface ActivityLogManager {
    /**
     * Returns entries from the activity log.
     *
     * @param entryCount the number of entries to return (starting from the end of the file)
     *
     * @return a List of ActivityLogEntry instances
     */
    List<ActivityLogEntry> getActivityLog(long entryCount);
}
