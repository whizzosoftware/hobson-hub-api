/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.image;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a named grouping of images.
 *
 * @author Dan Noguerol
 */
public class ImageGroup {
    private String id;
    private String name;
    private final List<String> imageIds = new ArrayList<>();

    public ImageGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public void addImageId(String id) {
        imageIds.add(id);
    }
}
