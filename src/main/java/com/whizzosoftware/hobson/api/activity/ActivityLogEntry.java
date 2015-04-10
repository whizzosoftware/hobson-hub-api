/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.activity;

/**
 * A log entry for an activity that occurred on the Hub.
 *
 * @author Dan Noguerol
 */
public class ActivityLogEntry {
    private Long timestamp;
    private String name;

    public ActivityLogEntry(Long timestamp, String name) {
        this.timestamp = timestamp;
        this.name = name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }
}
