/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.config.EmailConfiguration;

import java.util.Collection;

/**
 * A manager interface for Hub-related functions.
 *
 * @author Dan Noguerol
 */
public interface HubManager {
    /**
     * Returns the hubs associated with a user.
     *
     * @param userId the user ID that owns the hubs
     *
     * @return a Collection of Hub objects
     */
    public Collection<HobsonHub> getHubs(String userId);

    /**
     * Returns a specific hub associated with a user.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Hub object
     */
    public HobsonHub getHub(String userId, String hubId);

    /**
     * Returns content from the Hub log.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param startLine the starting line from the end of the file (e.g. 0 is the last line of the file)
     * @param endLine the ending line from the end of the file (e.g. 0 is the last line of the file)
     * @param appendable an Appendable to add the log content to
     *
     * @return a LineRage representing what was added to the appendable
     */
    public LineRange getLog(String userId, String hubId, long startLine, long endLine, Appendable appendable);

    /**
     * Returns a registrar interface for adding/removing new Hubs.
     *
     * @return a HubRegistrar interface
     */
    public HubRegistrar getRegistrar();

    /**
     * Returns a manager for the local hub (if one exists).
     *
     * @return a LocalHubManager or null if not available
     */
    public LocalHubManager getLocalManager();

    /**
     * Sets details for a hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param hub the new information (can be partially populated)
     */
    public void setHubDetails(String userId, String hubId, HobsonHub hub);

    /**
     * Clears (deletes) the details for a hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     */
    public void clearHubDetails(String userId, String hubId);

    /**
     * Sets the Hub password.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param change a PasswordChange instance
     *
     * @throws com.whizzosoftware.hobson.api.HobsonInvalidRequestException if password does not meet complexity requirements
     */
    public void setHubPassword(String userId, String hubId, PasswordChange change);

    /**
     * Sends a test e-mail message using the provided e-mail configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param config an EmailConfiguration instance
     *
     * @throws com.whizzosoftware.hobson.api.HobsonInvalidRequestException if the e-mail configuration is invalid
     */
    public void sendTestEmail(String userId, String hubId, EmailConfiguration config);

    /**
     * Sends an e-mail message using the provided e-mail configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param recipientAddress the e-mail address of the recipient
     * @param subject the e-mail subject line
     * @param body the e-mail message body
     */
    public void sendEmail(String userId, String hubId, String recipientAddress, String subject, String body);
}
