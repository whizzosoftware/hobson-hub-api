/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * An abstract base class for plugins that poll their devices using HTTP/HTTPS. It provides some convenience methods
 * for making HTTP calls asynchronously.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHttpClientPlugin extends AbstractHobsonPlugin implements ChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClientPlugin.class);

    private final NioEventLoopGroup httpEventLoopGroup;
    private Bootstrap bootstrap;
    private Bootstrap bootstrapSSL;
    private SslContext sslContext;

    public AbstractHttpClientPlugin(String pluginId) {
        super(pluginId);

        httpEventLoopGroup = new NioEventLoopGroup(1);

        try {
            sslContext = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } catch (Exception e) {
            logger.warn("Unable to create SSL engine; https will not work");
        }
    }

    /**
     * Sends an HTTP GET request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     */
    protected void sendHttpGetRequest(URI uri, Map<String,String> headers) {
        sendHttpRequest(uri, HttpMethod.GET, headers, null);
    }

    /**
     * Sends an HTTP POST request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     */
    protected void sendHttpPostRequest(URI uri, Map<String,String> headers, byte[] data) {
        sendHttpRequest(uri, HttpMethod.POST, headers, data);
    }

    /**
     * Sends an HTTP PUT request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     */
    protected void sendHttpPutRequest(URI uri, Map<String,String> headers, byte[] data) {
        sendHttpRequest(uri, HttpMethod.PUT, headers, data);
    }

    /**
     * Sends an HTTP DELETE request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     */
    protected void sendHttpDeleteRequest(URI uri, Map<String,String> headers, byte[] data) {
        sendHttpRequest(uri, HttpMethod.DELETE, headers, data);
    }

    /**
     * Sends an HTTP PATCH request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     */
    protected void sendHttpPatchRequest(URI uri, Map<String,String> headers, byte[] data) {
        sendHttpRequest(uri, HttpMethod.PATCH, headers, data);
    }

    /**
     * Callback for successful HTTP responses. Note that "successful" here refers to transport level success. For
     * example, a 500 status code response is still considered successful.
     *
     * @param statusCode the response status code
     * @param headers response headers
     * @param response the response body
     */
    abstract protected void onHttpResponse(int statusCode, List<Map.Entry<String,String>> headers, InputStream response);

    /**
     * Callback for unsuccessful HTTP responses. Note that "unsuccessful" means that there was a transport level
     * problem.
     *
     * @param cause the cause of the failure
     */
    abstract protected void onHttpRequestFailure(Throwable cause);

    private void sendHttpRequest(final URI uri, final HttpMethod method, final Map<String,String> headers, final byte[] content) {
        Bootstrap bs;
        int defaultPort;

        if (uri.getScheme().equalsIgnoreCase("https")) {
            bs = getBootstrapSSL();
            defaultPort = 443;
        } else {
            bs = getBootstrap();
            defaultPort = 80;
        }

        ChannelFuture future = bs.connect(uri.getHost(), (uri.getPort() > 0) ? uri.getPort() : defaultPort);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                DefaultFullHttpRequest request;
                if (content != null) {
                    request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.getRawPath(), Unpooled.wrappedBuffer(content));
                } else {
                    request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.getRawPath());
                }
                request.headers().set(HttpHeaders.Names.HOST, uri.getHost());
                request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
                request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
                if (headers != null) {
                    for (String name : headers.keySet()) {
                        request.headers().set(name, headers.get(name));
                    }
                }
                future.channel().writeAndFlush(request);
            }
        });
    }

    private Bootstrap getBootstrap() {
        if (bootstrap == null) {
            bootstrap = new Bootstrap();
            configureBootstrap(bootstrap);
            bootstrap.handler(new HttpClientInitializer(this, null));
        }
        return bootstrap;
    }

    private Bootstrap getBootstrapSSL() {
        if (bootstrapSSL == null && sslContext != null) {
            bootstrapSSL = new Bootstrap();
            configureBootstrap(bootstrapSSL);
            bootstrapSSL.handler(new HttpClientInitializer(this, sslContext));
        } else if (sslContext == null) {
            bootstrapSSL = bootstrap;
        }
        return bootstrapSSL;
    }

    private void configureBootstrap(Bootstrap b) {
        b.group(httpEventLoopGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.option(ChannelOption.SO_REUSEADDR, false);
        b.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
        b.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
    }

    /*
     * ChannelInboundHandler methods
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void channelRead(ChannelHandlerContext ctx, final Object msg) throws Exception {
        DefaultFullHttpResponse response = (DefaultFullHttpResponse)msg;
        final int statusCode = response.getStatus().code();
        final InputStream is = new ByteBufInputStream(response.content());
        final List<Map.Entry<String,String>> headers = response.headers().entries();
        executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                onHttpResponse(statusCode, headers, is);
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {}

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                onHttpRequestFailure(cause);
            }
        });
    }
}
