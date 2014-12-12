/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Inbound handler adapter used to preserve a request context Object.
 *
 * @author Dan Noguerol
 */
public class HttpClientResponseHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientResponseHandler.class);

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
        final ByteBuf buf = response.content();
        final List<Map.Entry<String,String>> headers = response.headers().entries();
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    is = new ByteBufInputStream(buf);
                    plugin.onHttpResponse(statusCode, headers, is, context);
                } catch (Exception e) {
                    logger.error("Error processing HTTP response", e);
                } finally {
                    buf.release();
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        logger.error("Error closing HTTP response stream", e);
                    }
                }
            }
        });
    }

    public void exceptionCaught(ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        plugin.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.onHttpRequestFailure(cause, context);
                } catch (Exception e) {
                    logger.error("Error processing HTTP failure", e);
                }
            }
        });
    }
}
