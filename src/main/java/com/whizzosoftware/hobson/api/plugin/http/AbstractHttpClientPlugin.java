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
abstract public class AbstractHttpClientPlugin extends AbstractHobsonPlugin {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClientPlugin.class);

    private final NioEventLoopGroup httpEventLoopGroup;
    private SslContext sslContext;

    public AbstractHttpClientPlugin(String pluginId) {
        super(pluginId);

        httpEventLoopGroup = new NioEventLoopGroup();

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
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    protected void sendHttpGetRequest(URI uri, Map<String,String> headers, Object context) {
        sendHttpRequest(uri, HttpMethod.GET, headers, null, context);
    }

    /**
     * Sends an HTTP POST request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    protected void sendHttpPostRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        sendHttpRequest(uri, HttpMethod.POST, headers, data, context);
    }

    /**
     * Sends an HTTP PUT request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    protected void sendHttpPutRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        sendHttpRequest(uri, HttpMethod.PUT, headers, data, context);
    }

    /**
     * Sends an HTTP DELETE request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    protected void sendHttpDeleteRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        sendHttpRequest(uri, HttpMethod.DELETE, headers, data, context);
    }

    /**
     * Sends an HTTP PATCH request. The response will be returned via the onHttpResponse() or onHttpRequestFailure()
     * callbacks.
     *
     * @param uri the URI to send the request to
     * @param headers request headers (or null if none)
     * @param data the request content
     * @param context a context object that will be returned in the response callback (or null if one is not needed)
     */
    protected void sendHttpPatchRequest(URI uri, Map<String,String> headers, byte[] data, Object context) {
        sendHttpRequest(uri, HttpMethod.PATCH, headers, data, context);
    }

    /**
     * Callback for successful HTTP responses. Note that "successful" here refers to transport level success. For
     * example, a 500 status code response is still considered successful.
     *
     * @param statusCode the response status code
     * @param headers response headers
     * @param response the response body
     * @param context the context object passed in the request
     */
    abstract protected void onHttpResponse(int statusCode, List<Map.Entry<String,String>> headers, InputStream response, Object context);

    /**
     * Callback for unsuccessful HTTP responses. Note that "unsuccessful" means that there was a transport level
     * problem.
     *
     * @param cause the cause of the failure
     * @param context the context object passed in the request
     */
    abstract protected void onHttpRequestFailure(Throwable cause, Object context);

    private void sendHttpRequest(final URI uri, final HttpMethod method, final Map<String,String> headers, final byte[] content, Object context) {
        Bootstrap bs;
        int defaultPort;

        if (uri.getScheme().equalsIgnoreCase("https")) {
            bs = getBootstrapSSL(context);
            defaultPort = 443;
        } else {
            bs = getBootstrap(context);
            defaultPort = 80;
        }

        ChannelFuture future = bs.connect(uri.getHost(), (uri.getPort() > 0) ? uri.getPort() : defaultPort);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                DefaultFullHttpRequest request;
                if (content != null) {
                    request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.getRawPath(), Unpooled.wrappedBuffer(content));
                    request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.length);
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

    private Bootstrap getBootstrap(Object context) {
        Bootstrap bootstrap = new Bootstrap();
        configureBootstrap(bootstrap);
        bootstrap.handler(new HttpClientInitializer(getId(), new HttpClientResponseHandler(this, context), null));
        return bootstrap;
    }

    private Bootstrap getBootstrapSSL(Object context) {
        if (sslContext != null) {
            Bootstrap bootstrapSSL = new Bootstrap();
            configureBootstrap(bootstrapSSL);
            bootstrapSSL.handler(new HttpClientInitializer(getId(), new HttpClientResponseHandler(this, context), sslContext));
            return bootstrapSSL;
        } else {
            return getBootstrap(context);
        }
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
}
