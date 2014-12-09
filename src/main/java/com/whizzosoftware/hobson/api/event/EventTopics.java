/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

/**
 * Provides static topic definitions.
 *
 * @author Dan Noguerol
 */
public class EventTopics {
    public static final String DEVICES_TOPIC = "com/whizzosoftware/hobson/event/devices";
    public static final String PLUGINS_TOPIC = "com/whizzosoftware/hobson/event/plugins";
    public static final String VARIABLES_TOPIC = "com/whizzosoftware/hobson/event/variables";
    public static final String PRESENCE_TOPIC = "com/whizzosoftware/hobson/event/presence";
    public static final String DISCO_TOPIC = "com/whizzosoftware/hobson/event/disco";

    public static String createDiscoTopic(String protocolId) {
        return DISCO_TOPIC + "/" + protocolId;
    }
}
