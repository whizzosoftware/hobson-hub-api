/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.image;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.List;

/**
 * An manager interface for dealing with images.
 *
 * @author Dan Noguerol
 */
public interface ImageManager {
    /**
     * Retrieves a Hub image.
     *
     * @return an ImageInputStream instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if an image does not exist
     */
    public ImageInputStream getHubImage(HubContext ctx);

    /**
     * Sets the current Hub image.
     *
     * @param ctx the context of the target hub
     */
    public void setHubImage(HubContext ctx, ImageInputStream iis);

    /**
     * Retrieves all groups in the image library.
     *
     * @param ctx the context of the target hub
     *
     * @return a List of ImageGroup objects
     */
    public List<ImageGroup> getImageLibraryGroups(HubContext ctx);

    /**
     * Retrieves all image IDs in a group.
     *
     * @param ctx the context of the target hub
     * @param groupId the group ID
     *
     * @return a List of Strings
     */
    public List<String> getImageLibraryImageIds(HubContext ctx, String groupId);

    /**
     * Retrieves binary representation of an image library image.
     *
     * @param ctx the context of the target hub
     * @param imageId the ID of the image to retrieve
     *
     * @return an ImageInputStream object
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if an image does not exist
     */
    public ImageInputStream getImageLibraryImage(HubContext ctx, String imageId);
}
