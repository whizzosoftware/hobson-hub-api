/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.channel;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A handler that receives Java objects from a channel and dispatches them to the plugin. It is also responsible
 * for channel idle detection and subsequent reconnections.
 *
 * @author Dan Noguerol
 */
public class ChannelObjectDriverInboundHandler extends SimpleChannelInboundHandler<Object> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AbstractChannelObjectPlugin plugin;
    private ChannelIdleDetectionConfig idleDetectionConfig;

    public ChannelObjectDriverInboundHandler(AbstractChannelObjectPlugin plugin) {
        this.plugin = plugin;
        this.idleDetectionConfig = plugin.getIdleDetectionConfig();
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {
        logger.trace("channelRead0: {}", o);
        plugin.onChannelData(o);
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) {
        logger.debug("channelUnregistered()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            ctx.writeAndFlush(idleDetectionConfig.getHeartbeatFrame().duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, event);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Exception reading from connection", cause);
        ctx.close();
    }
}
