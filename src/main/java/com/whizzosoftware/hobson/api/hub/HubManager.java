/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.Collection;

/**
 * A manager interface for Hub-related functions.
 *
 * @author Dan Noguerol
 */
public interface HubManager {
    /**
     * Returns the version of the hub.
     *
     * @param hubContext the context of the hub
     *
     * @return a String
     */
    String getVersion(HubContext hubContext);

    /**
     * Returns all hubs the system knows about.
     *
     * @return a Collection of HobsonHub instances
     */
    Collection<HubContext> getAllHubs();

    /**
     * Returns the hubs associated with a user.
     *
     * @param userId the user ID that owns the hubs
     *
     * @return a Collection of HobsonHub instances
     */
    Collection<HubContext> getHubs(String userId);

    /**
     * Returns a specific hub associated with a user.
     *
     * @param ctx the context of the hub to retrieve
     *
     * @return a Hub object
     */
    HobsonHub getHub(HubContext ctx);

    /**
     * Returns the user ID associated with the given hub ID.
     *
     * @param hubId the hub ID
     *
     * @return a String (or null if not found)
     */
    String getUserIdForHubId(String hubId);

    /**
     * Deletes the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     */
    void deleteConfiguration(HubContext ctx);

    /**
     * Indicates whether Hub credentials are valid.
     *
     * @param credentials the credentials to validate
     *
     * @return indicates whether the credentials are valid
     */
    boolean authenticateHub(HubCredentials credentials);

    /**
     * Returns the configuration class associated with a Hub.
     *
     * @param ctx the context of the hub
     *
     * @return a PropertyContainerClass instance
     */
    PropertyContainerClass getConfigurationClass(HubContext ctx);

    /**
     * Returns the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     *
     * @return a PropertyContainer instance containing the configuration
     */
    PropertyContainer getConfiguration(HubContext ctx);

    /**
     * Returns a published container class.
     *
     * @param ctx the context of the container class to return
     *
     * @return a PropertyContainerClass instance
     */
    PropertyContainerClass getContainerClass(PropertyContainerClassContext ctx);

    /**
     * Returns content from the Hub log.
     *
     * @param ctx the context of the hub that owns the logs
     * @param startLine the starting line from the end of the file (e.g. 0 is the last line of the file)
     * @param endLine the ending line from the end of the file (e.g. 0 is the last line of the file)
     * @param appendable an Appendable to add the log content to
     *
     * @return a LineRage representing what was added to the appendable
     */
    LineRange getLog(HubContext ctx, long startLine, long endLine, Appendable appendable);

    /**
     * Returns a manager for the local hub (if one exists).
     *
     * @return a LocalHubManager or null if not available
     */
    LocalHubManager getLocalManager();

    /**
     * Sends a test e-mail message using the provided e-mail configuration.
     *
     * @param ctx the context of the target hub
     * @param config an EmailConfiguration instance
     *
     * @throws com.whizzosoftware.hobson.api.HobsonInvalidRequestException if the e-mail configuration is invalid
     */
    void sendTestEmail(HubContext ctx, PropertyContainer config);

    /**
     * Sends an e-mail message using the provided e-mail configuration.
     *
     * @param ctx the context of the target hub
     * @param recipientAddress the e-mail address of the recipient
     * @param subject the e-mail subject line
     * @param body the e-mail message body
     */
    void sendEmail(HubContext ctx, String recipientAddress, String subject, String body);

    /**
     * Sets the configuration associated with a Hub.
     *
     * @param ctx the context of the hub
     * @param configuration the configuration to set
     */
    void setConfiguration(HubContext ctx, PropertyContainer configuration);
}
