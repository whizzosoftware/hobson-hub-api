/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

/**
 * Initializer for an HTTP client channel.
 *
 * @author Dan Noguerol
 */
public class HttpClientInitializer extends ChannelInitializer<Channel> {
    private String id;
    private ChannelInboundHandler handler;
    private SslContext sslContext;

    public HttpClientInitializer(String id, ChannelInboundHandler handler, SslContext sslContext) {
        this.id = id;
        this.handler = handler;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslContext != null) {
            pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));
        }
        pipeline.addLast("logger", new LoggingHandler("netty-" + id, LogLevel.TRACE));
        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("decompressor", new HttpContentDecompressor());
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        pipeline.addLast("handler", handler);
    }
}
