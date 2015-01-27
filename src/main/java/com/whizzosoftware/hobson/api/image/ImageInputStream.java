/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.image;

import java.io.IOException;
import java.io.InputStream;

/**
 * A wrapper class for an image input stream and its associated media type.
 *
 * @author Dan Noguerol
 */
public class ImageInputStream {
    private String mediaType;
    private InputStream inputStream;

    public ImageInputStream(String mediaType, InputStream inputStream) {
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    public String getMediaType() {
        return mediaType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void close() {
        try {
            inputStream.close();
        } catch (IOException ignored) {}
    }
}
