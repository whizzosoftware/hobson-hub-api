/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.TypedProperty;

/**
 * Defines the configurable properties for a Hub.
 *
 * @author Dan Noguerol
 */
public class HubConfigurationClass extends PropertyContainerClass {
    public static final String NAME = "name";
    public static final String SETUP_COMPLETE = "setupComplete";
    public static final String LOG_LEVEL = "logLevel";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public HubConfigurationClass() {
        addSupportedProperty(new TypedProperty(LATITUDE, "Latitude", "The Hub location's latitude", TypedProperty.Type.NUMBER));
        addSupportedProperty(new TypedProperty(LOG_LEVEL, "Log level", "The granularity of Hub logging", TypedProperty.Type.STRING));
        addSupportedProperty(new TypedProperty(LONGITUDE, "Longitude", "The Hub location's longitude", TypedProperty.Type.NUMBER));
        addSupportedProperty(new TypedProperty(NAME, "Hub name", "A descriptive name for the Hub (e.g. Home)", TypedProperty.Type.STRING));
        addSupportedProperty(new TypedProperty(SETUP_COMPLETE, "Setup completed", "Indicates whether the Hub setup process has been completed", TypedProperty.Type.BOOLEAN));
    }
}
