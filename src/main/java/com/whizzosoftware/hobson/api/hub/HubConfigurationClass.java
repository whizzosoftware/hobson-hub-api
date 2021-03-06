/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassType;
import com.whizzosoftware.hobson.api.property.TypedProperty;

/**
 * Defines the configurable properties for a Hub.
 *
 * @author Dan Noguerol
 */
public class HubConfigurationClass extends PropertyContainerClass {
    public static final String ID = "configuration";

    public static final String AWAY = "away";
    public static final String EMAIL_PASSWORD = "emailPassword";
    public static final String EMAIL_SECURE = "emailSecure";
    public static final String EMAIL_SENDER = "emailSender";
    public static final String EMAIL_SERVER = "emailServer";
    public static final String EMAIL_USER = "emailUsername";
    public static final String LATITUDE = "latitude";
    public static final String LOG_LEVEL = "logLevel";
    public static final String LONGITUDE = "longitude";
    public static final String NAME = "name";
    public static final String SETUP_COMPLETE = "setupComplete";
    public static final String SSL_MODE = "sslMode";

    public HubConfigurationClass() {
        super(PropertyContainerClassContext.create(HubContext.createLocal(), ID), PropertyContainerClassType.HUB_CONFIG);
        addSupportedProperty(new TypedProperty.Builder(EMAIL_PASSWORD, "E-mail password", "The e-mail password to use for sending mail", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(EMAIL_SECURE, "E-mail Secure", "Indicates whether the e-mail sending channel should be secure", TypedProperty.Type.BOOLEAN).build());
        addSupportedProperty(new TypedProperty.Builder(EMAIL_SENDER, "E-mail sender address", "The sender address to use for sending mail", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(EMAIL_SERVER, "E-mail server", "The e-mail server to use for sending mail", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(EMAIL_USER, "E-mail user", "The e-mail user to use for sending mail", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(LATITUDE, "Latitude", "The Hub location's latitude", TypedProperty.Type.NUMBER).build());
        addSupportedProperty(new TypedProperty.Builder(LOG_LEVEL, "Log level", "The granularity of Hub logging", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(LONGITUDE, "Longitude", "The Hub location's longitude", TypedProperty.Type.NUMBER).build());
        addSupportedProperty(new TypedProperty.Builder(NAME, "Hub name", "A descriptive name for the Hub (e.g. Home)", TypedProperty.Type.STRING).build());
        addSupportedProperty(new TypedProperty.Builder(SSL_MODE, "Secure mode", "When enabled, runs the hub in secure mode", TypedProperty.Type.BOOLEAN).build());
        addSupportedProperty(new TypedProperty.Builder(AWAY, "Away mode", "Indicates whether the Hub is in away mode", TypedProperty.Type.BOOLEAN).build());
        addSupportedProperty(new TypedProperty.Builder(SETUP_COMPLETE, "Setup completed", "Indicates whether the Hub setup process has been completed", TypedProperty.Type.BOOLEAN).build());
    }
}
