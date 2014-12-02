/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * A manager interface for Hub-related functions.
 *
 * @author Dan Noguerol
 */
public interface HubManager {
    /**
     * Returns the name of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a String
     */
    public String getHubName(String userId, String hubId);

    /**
     * Sets the name of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param name the name to set
     */
    public void setHubName(String userId, String hubId, String name);

    /**
     * Sets the Hub password.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param change a PasswordChange instance
     */
    public void setHubPassword(String userId, String hubId, PasswordChange change);

    /**
     * Authenticates the admin password.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param password the password to check
     *
     * @return true if the password is valid
     */
    public boolean authenticateAdmin(String userId, String hubId, String password);

    /**
     * Returns the location of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a HubLocation instance (or null if the location isn't set)
     */
    public HubLocation getHubLocation(String userId, String hubId);

    /**
     * Sets the location of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param location a HubLocation instance
     */
    public void setHubLocation(String userId, String hubId, HubLocation location);

    /**
     * Returns the e-mail configuration of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return an EmailConfiguration instance (or null if no e-mail information has been set)
     */
    public EmailConfiguration getHubEmailConfiguration(String userId, String hubId);

    /**
     * Sets the e-mail configuration of the Hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param config an EmailConfiguration instance
     */
    public void setHubEmailConfiguration(String userId, String hubId, EmailConfiguration config);

    /**
     * Indicates whether the Hub setup wizard has been completed.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a boolean
     */
    public boolean isSetupWizardComplete(String userId, String hubId);

    /**
     * Sets the Hub setup wizard completion status.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param complete the completion status (true == complete)
     */
    public void setSetupWizardComplete(String userId, String hubId, boolean complete);

    /**
     * Returns the current hub log level.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a String
     */
    public String getLogLevel(String userId, String hubId);

    /**
     * Sets the current hub log level. This will take effect immediately.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param level the new level
     */
    public void setLogLevel(String userId, String hubId, String level);

    /**
     * Returns content from the Hub log.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param startIndex the starting index into the log file
     * @param endIndex the ending index into the log file
     *
     * @return a LogContent instance
     */
    public LogContent getLog(String userId, String hubId, long startIndex, long endIndex);

    /**
     * Add a new appender for error logging.
     *
     * @param aAppender the appender to add
     */
    public void addErrorLogAppender(Object aAppender);

    /**
     * Remove an appender for error logging.
     *
     * @param aAppender the appender to remove
     */
    public void removeLogAppender(Object aAppender);
}
