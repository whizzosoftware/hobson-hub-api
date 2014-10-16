/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.impl;

import com.whizzosoftware.hobson.api.action.meta.ActionMeta;
import com.whizzosoftware.hobson.api.action.meta.ActionMetaEnumValue;
import com.whizzosoftware.hobson.api.action.meta.ActionMetaEnumValueParam;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * An action that can control a device.
 *
 * @author Dan Noguerol
 */
public class SendCommandToDeviceAction extends AbstractHobsonAction {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String TURN_OFF = "turnOff";
    private static final String TURN_ON = "turnOn";
    private static final String SET_LEVEL = "setLevel";

    private final Map<String,ActionMetaEnumValue> metaEnumValues = new HashMap<String,ActionMetaEnumValue>();

    public SendCommandToDeviceAction(String pluginId) {
        super(pluginId, "sendDeviceCommand", "Send Command to Device");

        buildMetaEnums();

        addMeta(new ActionMeta("pluginId", "Plugin ID", "The plugin that owns the device", ActionMeta.Type.STRING));
        addMeta(new ActionMeta("deviceId", "Device ID", "The device to send the command to", ActionMeta.Type.STRING));

        ActionMeta control = new ActionMeta("commandId", "Command", "The command to send to the device", ActionMeta.Type.ENUMERATION);
        for (ActionMetaEnumValue val : metaEnumValues.values()) {
            control.addEnumValue(val);
        }
        addMeta(control);
    }

    @Override
    public void execute(Map<String, Object> properties) {
        try {
            VariableUpdate vup = createVariableUpdate(properties);
            getVariableManager().setDeviceVariable(vup.getPluginId(), vup.getDeviceId(), vup.getName(), vup.getValue());
        } catch (Exception e) {
            logger.error("Error sending command to device", e);
        }
    }

    protected void buildMetaEnums() {
        metaEnumValues.put(TURN_OFF, new ActionMetaEnumValue(TURN_OFF, "Turn off", null, "on"));
        metaEnumValues.put(TURN_ON, new ActionMetaEnumValue(TURN_ON, "Turn on", null, "on"));
        metaEnumValues.put(SET_LEVEL, new ActionMetaEnumValue(SET_LEVEL, "Set level", new ActionMetaEnumValueParam("Level", "A percentage (0-100) for the device's level", ActionMeta.Type.NUMBER), "level"));
    }

    protected VariableUpdate createVariableUpdate(Map<String, Object> properties) {
        String commandId = (String)properties.get("commandId");
        return new VariableUpdate(
            (String)properties.get("pluginId"),
            (String)properties.get("deviceId"),
            getVariableNameForCommandId(commandId),
            getVariableValueForCommandId(commandId, properties.get("param"))
        );
    }

    protected String getVariableNameForCommandId(String commandId) {
        ActionMetaEnumValue amev = metaEnumValues.get(commandId);
        if (amev != null) {
            return amev.getRequiredDeviceVariable();
        } else {
            throw new IllegalArgumentException("Invalid commandId: " + commandId);
        }
    }

    protected Object getVariableValueForCommandId(String commandId, Object param) {
        if (TURN_ON.equals(commandId)) {
            return true;
        } else if (TURN_OFF.equals(commandId)) {
            return false;
        } else if (SET_LEVEL.equals(commandId)) {
            return param;
        } else {
            throw new IllegalArgumentException("Invalid commandId: " + commandId);
        }
    }
}
