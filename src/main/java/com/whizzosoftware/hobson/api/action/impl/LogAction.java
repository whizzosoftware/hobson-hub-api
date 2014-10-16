/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.impl;

import com.whizzosoftware.hobson.api.action.meta.ActionMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * An action that can send messages to the log file.
 *
 * @author Dan Noguerol
 */
public class LogAction extends AbstractHobsonAction {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LogAction(String pluginId) {
        super(pluginId, "log", "Log Message");

        addMeta(new ActionMeta("message", "Message", "The message added to the log file", ActionMeta.Type.STRING));
    }

    @Override
    public void execute(Map<String, Object> properties) {
        String message = (String)properties.get("message");
        if (message != null) {
            logger.info(message);
        } else {
            logger.error("No log message specified; unable to execute log action");
        }
    }
}
