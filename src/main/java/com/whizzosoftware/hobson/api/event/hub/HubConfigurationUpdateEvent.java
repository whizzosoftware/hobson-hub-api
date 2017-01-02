/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.hub;

import com.whizzosoftware.hobson.api.property.PropertyContainer;

import java.util.Map;

/**
 * Event that occurs when the hub's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class HubConfigurationUpdateEvent extends HubEvent {
    public static final String ID = "hubConfigurationUpdate";

    private static final String PROP_CONFIGURATION = "configuration";

    public HubConfigurationUpdateEvent(long timestamp, PropertyContainer config) {
        super(timestamp, ID);
        setProperty(PROP_CONFIGURATION, config);
    }

    public HubConfigurationUpdateEvent(Map<String,Object> props) {
        super(props);
    }

    public PropertyContainer getConfiguration() {
        return (PropertyContainer)getProperty(PROP_CONFIGURATION);
    }
}
