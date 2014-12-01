/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * Encapsulates information about log content including the content itself as well as the start/end index of
 * that content in the log file.
 *
 * @author Dan Noguerol
 */
public class LogContent {
    private long startIndex;
    private long endIndex;
    private byte[] bytes;

    public LogContent(long startIndex, long endIndex, byte[] bytes) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.bytes = bytes;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
