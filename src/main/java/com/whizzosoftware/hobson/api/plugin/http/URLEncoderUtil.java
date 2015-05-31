/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * A class with convenience methods for URL and form encoding.
 *
 * @author Dan Noguerol
 */
public class URLEncoderUtil {
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * Creates a URI query string from a map of parameters and encoding.
     *
     * @param parameters a map of parameters
     * @param encoding the encoding for the resultant string
     *
     * @return a URI String
     * @throws UnsupportedEncodingException on failure
     */
    public static String createQueryString(Map<String, String> parameters, final String encoding) throws UnsupportedEncodingException {
        final StringBuilder result = new StringBuilder();
        for (String key : parameters.keySet()) {
            final String encodedKey = URLEncoder.encode(key, (encoding != null ? encoding : DEFAULT_ENCODING));
            final String value = parameters.get(key);
            final String encodedValue;
            if (value != null) {
                encodedValue = URLEncoder.encode(value, encoding != null ? encoding : DEFAULT_ENCODING);
            } else {
                encodedValue = "";
            }
            if (result.length() > 0) {
                result.append(PARAMETER_SEPARATOR);
            }
            result.append(encodedKey);
            result.append(KEY_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }
}
