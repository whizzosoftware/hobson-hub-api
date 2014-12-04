/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util;

import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * "Variable change IDs" are IDs that identify a specific change of a variable. For example, the change ID
 * "turnOff" defines the situation when an "on" variable becomes "false". This provides a user-friendly
 * mechanism for defining simple variable change events.
 *
 * @author Dan Noguerol
 */
public class VariableChangeIdHelper {
    public static final String TURN_ON = "turnOn";
    public static final String TURN_OFF = "turnOff";
    public static final String SET_LEVEL = "setLevel%d";

    /**
     * Add a list of change IDs to the supplied List that correspond to the variable name.
     *
     * @param name the variable name
     * @param changeIds the List to add the change IDs to
     */
    static public void populateChangeIdsForVariableName(String name, List<String> changeIds) {
        switch (name) {
            case VariableConstants.ON:
                changeIds.add(TURN_ON);
                changeIds.add(TURN_OFF);
                break;
            case VariableConstants.LEVEL:
                changeIds.add(SET_LEVEL);
                break;
        }
    }

    /**
     * Returns the variable name/value that corresponds to a change ID.
     *
     * @param changeId the change ID
     *
     * @return a Map of variable name to variable value
     */
    static public Map<String,Object> getVariableForChangeId(String changeId) {
        Map<String,Object> result = new HashMap<>();
        switch (changeId) {
            case TURN_ON:
                result.put(VariableConstants.ON, true);
                break;
            case TURN_OFF:
                result.put(VariableConstants.ON, false);
                break;
            case SET_LEVEL:
                // TODO: parse the integer
                result.put(VariableConstants.LEVEL, 100);
        }
        return result;
    }
}
