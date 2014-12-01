/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.meta;

/**
 * Represents a parameter for an action meta enumeration.
 *
 * For example, the "Send Command to Device" action's "Set Level" enumeration takes a numeric parameter representing
 * the level value.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public class ActionMetaDataEnumValueParam {
    private String name;
    private String description;
    private ActionMetaData.Type type;

    /**
     * Constructor.
     *
     * @param name the enum value parameter name
     * @param description the enum value parameter description
     * @param type the enum value parameter type
     */
    public ActionMetaDataEnumValueParam(String name, String description, ActionMetaData.Type type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ActionMetaData.Type getType() {
        return type;
    }
}
