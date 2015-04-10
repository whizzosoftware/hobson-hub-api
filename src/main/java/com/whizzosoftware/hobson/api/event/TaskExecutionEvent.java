/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

/**
 * Event that occurs when a task is executed.
 *
 * @author Dan Noguerol
 */
public class TaskExecutionEvent extends HobsonEvent {
    public static final String ID = "taskExecute";

    private static final String PROP_NAME = "name";

    public TaskExecutionEvent(long timestamp, String name) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        setProperty(PROP_NAME, name);
    }

    public String getName() {
        return (String)getProperty(PROP_NAME);
    }
}
