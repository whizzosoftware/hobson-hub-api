/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************
*/
package com.whizzosoftware.hobson.api.hub;

/**
 * Represents an OpenID Connect configuration.
 *
 * @author Dan Noguerol
 */
public class OIDCConfig {
    private String issuer;
    private String authorizationEndpoint;
    private String tokenEndpoint;
    private String userInfoEndpoint;
    private String jwksEndpoint;
    private String[] responseTypesSupported;
    private String[] grantTypesSupported;
    private String[] subjectTypesSupported;
    private String[] idTokenSigningAlgValuesSupported;
    private Object signingKey;

    public OIDCConfig(String issuer, String authorizationEndpoint, String tokenEndpoint, String userInfoEndpoint, String jwksEndpoint, Object signingKey) {
        this.issuer = issuer;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.userInfoEndpoint = userInfoEndpoint;
        this.jwksEndpoint = jwksEndpoint;
        this.signingKey = signingKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public String getUserInfoEndpoint() {
        return userInfoEndpoint;
    }

    public String getJwksEndpoint() {
        return jwksEndpoint;
    }

    public String[] getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(String[] responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public String[] getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(String[] subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public String[] getGrantTypesSupported() {
        return grantTypesSupported;
    }

    public void setGrantTypesSupported(String[] grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }

    public String[] getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public void setIdTokenSigningAlgValuesSupported(String[] idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }

    public Object getSigningKey() {
        return signingKey;
    }
}
