/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

/**
 * A class that represents the current status of a Hobson plugin.
 *
 * @author Dan Noguerol
 */
public class PluginStatus {
    private Code code;
    private String message;

    /**
     * Constructor.
     *
     * @param code the plugin status
     */
    public PluginStatus(Code code) {
        this(code, null);
    }

    /**
     * Constructor.
     *
     * @param code the plugin status
     * @param message a message associated with the plugin status
     */
    public PluginStatus(Code code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param status the plugin status as a String
     * @param message a message associated with the plugin status
     */
    public PluginStatus(String status, String message) {
        this.code = Code.valueOf(status.toUpperCase());
        this.message = message;
    }

    /**
     * Returns a status enumeration.
     *
     * @return a Status
     */
    public Code getCode() {
        return code;
    }

    /**
     * Returns a human-readable message regarding the plugin status (for example, the cause of a failure)
     *
     * @return a String
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return this.code.name();
    }

    public static PluginStatus notInstalled() {
        return new PluginStatus(Code.NOT_INSTALLED);
    }

    public static PluginStatus notInstalled(String message) {
        return new PluginStatus(Code.NOT_INSTALLED, message);
    }

    public static PluginStatus stopped() {
        return new PluginStatus(Code.STOPPED);
    }

    public static PluginStatus stopped(String message) {
        return new PluginStatus(Code.STOPPED, message);
    }

    public static PluginStatus initializing() {
        return new PluginStatus(Code.INITIALIZING);
    }

    public static PluginStatus notConfigured(String message) {
        return new PluginStatus(Code.NOT_CONFIGURED, message);
    }

    public static PluginStatus failed(String message) {
        return new PluginStatus(Code.FAILED, message);
    }

    public static PluginStatus running() {
        return new PluginStatus(Code.RUNNING);
    }

    public static PluginStatus configChange() {
        return new PluginStatus(Code.CONFIG_CHANGE);
    }

    public static PluginStatus stopping() {
        return new PluginStatus(Code.STOPPING);
    }

    public enum Code {
        /**
         * The plugin is not installed.
         */
        NOT_INSTALLED,
        /**
         * The plugin is installed but not running.
         */
        STOPPED,
        /**
         * The plugin is initializing.
         */
        INITIALIZING,
        /**
         * The plugin has not been configured.
         */
        NOT_CONFIGURED,
        /**
         * The plugin failed to start.
         */
        FAILED,
        /**
         * The plugin is successfully running.
         */
        RUNNING,
        /**
         * The plugin has a configuration change pending.
         */
        CONFIG_CHANGE,
        /**
         * The plugin is in the process of stopping.
         */
        STOPPING
    }
}
