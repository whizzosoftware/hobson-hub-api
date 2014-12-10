/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Inbound handler adapter used to preserve a request context Object.
 *
 * @author Dan Noguerol
 */
public class HttpClientResponseHandler extends ChannelInboundHandlerAdapter {
    private AbstractHttpClientPlugin plugin;
    private Object context;

    public HttpClientResponseHandler(AbstractHttpClientPlugin plugin, Object context) {
        this.plugin = plugin;
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, final Object msg) throws Exception {
        DefaultFullHttpResponse response = (DefaultFullHttpResponse)msg;
        final int statusCode = response.getStatus().code();
        final InputStream is = new ByteBufInputStream(response.content());
        final List<Map.Entry<String,String>> headers = response.headers().entries();
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                plugin.onHttpResponse(statusCode, headers, is, context);
            }
        });
    }

    public void exceptionCaught(ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                plugin.onHttpRequestFailure(cause, context);
            }
        });
    }
}
