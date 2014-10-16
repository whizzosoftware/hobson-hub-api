/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.meta;

/**
 * Represents an optional parameter for an action meta enumeration.
 *
 * For example, the "Send Command to Device" action's "Set Level" enumeration takes a numeric parameter representing
 * the level value.
 *
 * @author Dan Noguerol
 */
public class ActionMetaEnumValueParam {
    private String name;
    private String description;
    private ActionMeta.Type type;

    public ActionMetaEnumValueParam(String name, String description, ActionMeta.Type type) {
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

    public ActionMeta.Type getType() {
        return type;
    }
}
