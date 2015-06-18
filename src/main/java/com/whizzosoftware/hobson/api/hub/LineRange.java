/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * Represents a range of lines (e.g. in a log file).
 *
 * @author Dan Noguerol
 */
public class LineRange {
    private Long startLine;
    private Long endLine;

    /**
     * Constructor.
     *
     * @param httpHeaderValue a range string in RFC-7233 format
     */
    public LineRange(String httpHeaderValue) {
        String s1 = httpHeaderValue.trim();
        if (s1.startsWith("lines=")) {
            try {
                String s2 = s1.substring(6);
                int ix = s2.indexOf('-');
                if (ix == 0) {
                    endLine = Long.parseLong(s2.substring(1));
                } else if (ix == s2.length() - 1) {
                    startLine = Long.parseLong(s2.substring(0, s2.length() - 1));
                } else {
                    startLine = Long.parseLong(s2.substring(0, ix));
                    endLine = Long.parseLong(s2.substring(ix+1, s2.length()));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Range values must be numeric");
            }
        } else {
            throw new IllegalArgumentException("Only line ranges (lines=) are supported");
        }
    }

    /**
     * Constructor.
     *
     * @param startLine the start line of the range
     * @param endLine the end line of the range
     */
    public LineRange(Long startLine, Long endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public boolean hasStartLine() {
        return (startLine != null);
    }

    public Long getStartLine() {
        return startLine;
    }

    public boolean hasEndLine() {
        return (endLine != null);
    }

    public Long getEndLine() {
        return endLine;
    }

    public Long getLineCount() {
        return (startLine != null && endLine != null) ? endLine - startLine : 0;
    }

    public String toString() {
        if (hasStartLine() && hasEndLine()) {
            return "lines=" + getStartLine() + "-" + getEndLine() + "/*";
        } else if (hasStartLine()) {
            return "lines=" + getStartLine() + "-" + "/*";
        } else if (hasEndLine()) {
            return "lines=-" + getEndLine() + "/*";
        }
        return "lines=";
    }
}
