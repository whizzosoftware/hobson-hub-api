/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A property that specifies a type (such as STRING, NUMBER, etc).
 *
 * @author Dan Noguerol
 */
public class TypedProperty implements Serializable { // TODO: remove
    private String id;
    private String name;
    private String description;
    private Type type;
    private Object[] enumeration;
    private List<TypedPropertyConstraint> constraints;

    /**
     * Constructor.
     *
     * @param id the ID of the property
     * @param name a human-readable name for the property
     * @param description a human-readable description for the property
     * @param type the property type
     * @param constraints a set of constraints that the property must adhere to
     */
    private TypedProperty(String id, String name, String description, Type type, Object[] enumeration, List<TypedPropertyConstraint> constraints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.enumeration = enumeration;
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

    public Object[] getEnumeration() {
        return enumeration;
    }

    public Collection<TypedPropertyConstraint> getConstraints() {
        return constraints;
    }

    public TypedProperty addConstraint(PropertyConstraintType type, Object argument) {
        if (constraints == null) {
            constraints = new ArrayList<>();
        }
        constraints.add(new TypedPropertyConstraint(type, argument));
        return this;
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
            for (TypedPropertyConstraint tpc : constraints) {
                switch (tpc.getType()) {
                    case deviceVariable:
                        if (!publishedVariableNames.contains(tpc.getArgument().toString())) {
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
        LOCATION,
        NUMBER,
        PRESENCE_ENTITY,
        RECURRENCE,
        SECURE_STRING,
        STRING,
        TIME
    }
    public static class Builder {
        private TypedProperty prop;

        public Builder(String id, String name, String description, Type type) {
            prop = new TypedProperty(id, name, description, type, null, null);
        }

        public Builder enumeration(Object[] enumeration) {
            if ((prop.type == Type.STRING && enumeration instanceof String[]) || (prop.type == Type.NUMBER && enumeration instanceof Integer[])) {
                prop.enumeration = enumeration;
                return this;
            } else {
                throw new IllegalArgumentException("Only enumerations of strings and integers are currently supported");
            }
        }

        public Builder constraint(PropertyConstraintType type, Object argument) {
            prop.addConstraint(type, argument);
            return this;
        }

        public TypedProperty build() {
            return prop;
        }
    }
}
