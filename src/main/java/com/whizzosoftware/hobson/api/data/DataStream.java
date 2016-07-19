/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.data;

import java.util.Collection;
import java.util.Set;

/**
 * A named collection fields comprising a temporal stream of data.
 *
 * @author Dan Noguerol
 */
public class DataStream {
    private String id;
    private String name;
    private Collection<DataStreamField> fields;
    private Set<String> tags;

    public DataStream(String id, String name, Collection<DataStreamField> fields, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.fields = fields;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasFields() {
        return (fields != null && fields.size() > 0);
    }

    public Collection<DataStreamField> getFields() {
        return fields;
    }

    public boolean hasTags() {
        return (tags != null && tags.size() > 0);
    }

    public Set<String> getTags() {
        return tags;
    }
}
