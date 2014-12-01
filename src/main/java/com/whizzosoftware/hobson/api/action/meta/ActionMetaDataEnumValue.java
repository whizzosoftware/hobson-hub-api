/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.meta;

/**
 * Represents a valid value for an action meta enumeration. This is used when an ActionMeta is of type ENUMERATION.
 *
 * For example, the "Send Command to Device" action might have enumerated values of "Turn On", "Turn Off" and
 * "Set Level". The "Turn On" and "Turn Off" values would have implicit parameters (e.g. "on" and "off" respectively).
 * The "Set Level" value, on the other hand, requires a parameter indicating what the level should be set to.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public class ActionMetaDataEnumValue {
    private String id;
    private String name;
    private ActionMetaDataEnumValueParam param;
    private String requiredDeviceVariable;

    /**
     * Constructor.
     *
     * @param id the ID of the enum value
     * @param name the name of the enum value
     * @param param the enum parameter
     * @param requiredDeviceVariable the device variable that the device must have in order for this enum value to be applicable
     */
    public ActionMetaDataEnumValue(String id, String name, ActionMetaDataEnumValueParam param, String requiredDeviceVariable) {
        this.id = id;
        this.name = name;
        this.param = param;
        this.requiredDeviceVariable = requiredDeviceVariable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ActionMetaDataEnumValueParam getParam() {
        return param;
    }

    public String getRequiredDeviceVariable() {
        return requiredDeviceVariable;
    }
}
