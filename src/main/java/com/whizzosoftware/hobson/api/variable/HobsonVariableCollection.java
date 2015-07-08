/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing an unordered collection of HobsonVariable objects.
 *
 * @author Dan Noguerol
 */
public class HobsonVariableCollection {
    private Map<String,HobsonVariable> variableMap = new HashMap<>();

    public HobsonVariableCollection(Collection<HobsonVariable> variables) {
        for (HobsonVariable var : variables) {
            add(var);
        }
    }

    public Collection<HobsonVariable> getCollection() {
        return variableMap.values();
    }

    public HobsonVariable get(String name) {
        return variableMap.get(name);
    }

    public void add(HobsonVariable v) {
        variableMap.put(v.getName(), v);
    }
}
