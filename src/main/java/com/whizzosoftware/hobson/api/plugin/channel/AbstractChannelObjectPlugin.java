/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.channel;

import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import com.whizzosoftware.hobson.bootstrap.api.plugin.PluginStatus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Dictionary;
import java.util.concurrent.TimeUnit;

/**
 * An abstract base class for plugins that read/write data to/from a Netty channel as Java objects. The channel is
 * an abstraction for both network and serial port connections.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractChannelObjectPlugin extends AbstractHobsonPlugin {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String serialDevice;
    private Channel channel;
    private SocketAddress socketAddress;
    private State connectionState = State.NOT_CONNECTED;
    private boolean isRunning = true;

    public AbstractChannelObjectPlugin(String pluginId) {
        super(pluginId);
    }

    @Override
    public void init(Dictionary config) {
        processConfig(config);
    }

    private void processConfig(Dictionary config) {
        boolean didConfigChange = false;

        if (config != null) {
            String s = (String) config.get("serial.port");
            if (s != null && s.trim().length() > 0 && !s.equals(serialDevice)) {
                serialDevice = s;
                setRemoteAddress(new RxtxDeviceAddress(serialDevice));
                didConfigChange = true;
            } else {
                s = (String) config.get("serial.hostname");
                if (s != null && s.trim().length() > 0 && !s.equals(serialDevice)) {
                    serialDevice = s;
                    setRemoteAddress(new InetSocketAddress(serialDevice, 4999));
                    didConfigChange = true;
                }
            }
        }

        if (serialDevice == null) {
            setStatus(new PluginStatus(PluginStatus.Status.NOT_CONFIGURED, "Neither serial port nor serial hostname are configured"));
        } else if (didConfigChange) {
            // disconnect if we're already connected
            if (connectionState == State.CONNECTED) {
                disconnect();
            } else {
                // attempt to connect
                try {
                    connect();
                    setStatus(new PluginStatus(PluginStatus.Status.RUNNING));
                } catch (IOException e) {
                    logger.error("Error attempting to connect", e);
                    setStatus(new PluginStatus(PluginStatus.Status.FAILED, e.getLocalizedMessage()));
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        // NO-OP
    }

    @Override
    public void stop() {
        isRunning = false;
        disconnect();
    }

    @Override
    public void onPluginConfigurationUpdate(Dictionary config) {
        processConfig(config);
    }

    /**
     * Sets the remote address to use as the data channel.
     *
     * @param socketAddress the address
     */
    public void setRemoteAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    /**
     * Indicates whether the channel is a network connection.
     *
     * @return a boolean
     */
    protected boolean isNetworkAddress() {
        return (socketAddress instanceof InetSocketAddress);
    }

    /**
     * Indicates whether the channel is a serial port.
     *
     * @return a boolean
     */
    protected boolean isSerialAddress() {
        return (socketAddress instanceof RxtxDeviceAddress);
    }

    /**
     * Sends an object over the channel. It will be the job of the handler returned from getEncoder() to convert
     * the object into bytes for transmission over the channel.
     *
     * @param o the object to send
     */
    protected void send(Object o) {
        logger.debug("Sending: " + o);
        if (channel != null) {
            channel.writeAndFlush(o);
        } else {
            logger.warn("Unable to send command because channel has not yet been established");
        }
    }

    /**
     * Returns a configuration object used for idle detection.
     *
     * @return an IdleDetectionConfig instance (or null if no idle detection should be performed).
     */
    protected ChannelIdleDetectionConfig getIdleDetectionConfig() {
        return null;
    }

    /**
     * Returns the decoder that converts a sequence of bytes received from the channel into one or more
     * Java objects.
     *
     * @return a ChannelInboundHandlerAdapter
     */
    abstract protected ChannelInboundHandlerAdapter getDecoder();

    /**
     * Returns an encoder that converts a Java object into a sequence of bytes from transmission over the
     * channel.
     *
     * @return a ChannelOutboundHandlerAdapter
     */
    abstract protected ChannelOutboundHandlerAdapter getEncoder();

    /**
     * Allows subclasses to configure the channel before it is used.
     *
     * @param config the channel configuration
     */
    abstract protected void configureChannel(ChannelConfig config);

    /**
     * Called when the channel successfully connects.
     */
    abstract protected void onChannelConnected();

    /**
     * Called when the channel receives data.
     *
     * @param o the data received
     */
    abstract protected void onChannelData(Object o);

    /**
     * Called when the channel disconnected.
     */
    abstract protected void onChannelDisconnected();

    private Bootstrap configureBootstrap(Bootstrap b) throws IOException {
        return configureBootstrap(b, createEventLoopGroup());
    }

    private Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
        b.group(g);

        // configure for either network or serial channel
        if (isNetworkAddress()) {
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.config().setConnectTimeoutMillis(5000);
                    configureChannel(channel.config());
                    configurePipeline(channel.pipeline());
                }
            });
        } else {
            b.channel(RxtxChannel.class);
            b.handler(new ChannelInitializer<RxtxChannel>() {
                @Override
                public void initChannel(RxtxChannel channel) throws Exception {
                    configureChannel(channel.config());
                    configurePipeline(channel.pipeline());
                }
            });
        }

        return b;
    }

    /**
     * Creates the appropriate EventLoopGroup based on whether the remote address is a network address or serial port.
     *
     * @return an EventLoopGroup
     *
     * @throws IOException on failure
     */
    private EventLoopGroup createEventLoopGroup() throws IOException {
        if (isNetworkAddress()) {
            return new NioEventLoopGroup();
        } else if (isSerialAddress()) {
            return new OioEventLoopGroup();
        } else {
            throw new IOException("Remote address is neither network or serial");
        }
    }

    /**
     * Configures the Netty channel pipeline. This can be overridden by subclasses if needed.
     *
     * @param pipeline the current channel pipeline
     */
    private void configurePipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        if (getIdleDetectionConfig() != null) {
            pipeline.addLast("idle", new IdleStateHandler(0, 0, getIdleDetectionConfig().getMaxIdleTime(), TimeUnit.SECONDS));
        }
        pipeline.addLast("client", new ChannelObjectDriverInboundHandler(this));
    }

    /**
     * Connect the channel.
     *
     * @throws IOException on failure
     */
    private void connect() throws IOException {
        connect(configureBootstrap(new Bootstrap()));
    }

    /**
     * Connect the channel.
     *
     * @param b a Netty Bootstrap object
     */
    synchronized private void connect(Bootstrap b) {
        logger.debug("connect()");
        if (connectionState == State.NOT_CONNECTED) {
            logger.debug("Attempting to connect");
            connectionState = State.CONNECTING;
            b.connect(socketAddress).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        logger.debug("Connection established");
                        connectionState = State.CONNECTED;

                        // save the channel
                        AbstractChannelObjectPlugin.this.channel = channelFuture.channel();

                        // set a close listener to notify the plugin subclass when the channel has closed
                        channel.closeFuture().addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                logger.info("Connection was closed");
                                channel = null;
                                connectionState = State.NOT_CONNECTED;
                                onChannelDisconnected();
                                if (isRunning) {
                                    scheduleReconnect(channelFuture.channel().eventLoop());
                                }
                            }
                        });

                        onChannelConnected();
                    } else {
                        logger.error("Connection attempt failed", channelFuture.cause());
                        connectionState = State.NOT_CONNECTED;
                        if (isRunning) {
                            scheduleReconnect(channelFuture.channel().eventLoop());
                        }
                    }
                }
            });
        } else {
            logger.debug("Ignoring connect request due to state: " + connectionState);
        }
    }

    synchronized private void disconnect() {
        logger.debug("disconnect()");
        if (channel != null) {
            logger.debug("Closing connection");
            // remove the client handler so that a reconnect does not occur
            channel.pipeline().remove("client");
            // close the channel
            channel.close();
        }
    }

    private void scheduleReconnect(final EventLoop eventLoop) {
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                logger.debug("Will attempt reconnect in 5 seconds");
                try {
                    connect();
                } catch (IOException e) {
                    logger.error("Connection attempt failed", e);
                    if (isRunning) {
                        scheduleReconnect(eventLoop);
                    }
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    private enum State {
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED
    }
}
