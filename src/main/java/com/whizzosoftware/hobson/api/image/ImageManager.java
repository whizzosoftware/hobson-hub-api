/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.image;

import java.util.List;

/**
 * An manager interface for dealing with images.
 *
 * @author Dan Noguerol
 */
public interface ImageManager {
    /**
     * Retrieves the current Hub image.
     *
     * @return an ImageInputStream instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if an image does not exist
     */
    public ImageInputStream getHubImage();

    /**
     * Sets the current Hub image.
     *
     * @param iis an ImageInputStream instance
     */
    public void setHubImage(ImageInputStream iis);

    /**
     * Retrieves all groups in the image library.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a List of ImageGroup objects
     */
    public List<ImageGroup> getImageLibraryGroups(String userId, String hubId);

    /**
     * Retrieves all image IDs in a group.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param groupId the group ID
     *
     * @return a List of Strings
     */
    public List<String> getImageLibraryImageIds(String userId, String hubId, String groupId);

    /**
     * Retrieves binary representation of an image library image.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param imageId the ID of the image to retrieve
     *
     * @return an ImageInputStream object
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if an image does not exist
     */
    public ImageInputStream getImageLibraryImage(String userId, String hubId, String imageId);
}
