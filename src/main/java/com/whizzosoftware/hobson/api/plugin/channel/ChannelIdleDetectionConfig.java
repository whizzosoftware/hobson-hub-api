/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * Configuration that defines a plugin's channel idle detection configuration.
 *
 * @author Dan Noguerol
 */
public class ChannelIdleDetectionConfig {
    private int maxIdleTime;
    private ByteBuf heartbeatFrame;

    /**
     * Constructor.
     *
     * @param maxIdleTime the maximum number of seconds a channel can be idle before a heartbeat is sent
     * @param heartbeatFrame the heartbeat frame to send when a channel is considered idle
     */
    public ChannelIdleDetectionConfig(int maxIdleTime, String heartbeatFrame) {
        this.maxIdleTime = maxIdleTime;
        this.heartbeatFrame = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(heartbeatFrame, CharsetUtil.UTF_8));
    }

    /**
     * Returns the maximum number of seconds a channel can be idle before a heartbeat is sent.
     *
     * @return an int
     */
    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * Returns the heartbeat frame that is sent when a channel is considered idle.
     *
     * @return a ByteBuf
     */
    public ByteBuf getHeartbeatFrame() {
        return heartbeatFrame;
    }
}
