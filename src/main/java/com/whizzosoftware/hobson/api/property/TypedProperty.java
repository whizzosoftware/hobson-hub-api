/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.util.Collection;
import java.util.Map;

/**
 * A property that specifies a type (such as STRING, NUMBER, etc).
 *
 * @author Dan Noguerol
 */
public class TypedProperty {
    private String id;
    private String name;
    private String description;
    private Type type;
    private Map<TypedPropertyConstraint,String> constraints;

    public TypedProperty(String id) {
        this(id, null, null, null, null);
    }

    public TypedProperty(String id, String name, String description, Type type) {
        this(id, name, description, type, null);
    }

    /**
     * Constructor.
     *
     * @param id the ID of the property
     * @param name a human-readable name for the property
     * @param description a human-readable description for the property
     * @param type the property type
     * @param constraints a set of constraints that the property must adhere to
     */
    public TypedProperty(String id, String name, String description, Type type, Map<TypedPropertyConstraint,String> constraints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.constraints = constraints;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Map<TypedPropertyConstraint,String> getConstraints() {
        return constraints;
    }

    /**
     * Evaluates whether all defined constraints are met.
     *
     * @param publishedVariableNames a list of all published variable names
     *
     * @return a boolean
     */
    public boolean evaluateConstraints(Collection<String> publishedVariableNames) {
        if (constraints != null) {
            for (TypedPropertyConstraint tpc : constraints.keySet()) {
                switch (tpc) {
                    case deviceVariable:
                        if (!publishedVariableNames.contains(constraints.get(tpc))) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    public enum Type {
        BOOLEAN,
        COLOR,
        DATE,
        DEVICE,
        DEVICES,
        RECURRENCE,
        NUMBER,
        SECURE_STRING,
        STRING,
        TIME
    }
}
