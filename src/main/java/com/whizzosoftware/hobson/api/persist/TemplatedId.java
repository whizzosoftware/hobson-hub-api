/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.persist;

/**
 * An entity ID that has an associated template to facilitate parsing of the ID. This is used as a workaround
 * for the currently unfortunate situation that most REST documentation and client library generation tools do
 * not have good support for hypermedia.
 *
 * @author Dan Noguerol
 */
public class TemplatedId {
    private String id;
    private String idTemplate;

    public TemplatedId(String id, String idTemplate) {
        this.id = id;
        this.idTemplate = idTemplate;
    }

    public String getId() {
        return id;
    }

    public String getIdTemplate() {
        return idTemplate;
    }
}
