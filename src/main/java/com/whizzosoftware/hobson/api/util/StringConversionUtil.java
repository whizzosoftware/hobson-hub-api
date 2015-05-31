/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.DeviceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class that converts between Java primitives/objects and Strings preserving the data type.
 *
 * N = null
 * I = integer
 * D = double
 * B = boolean
 * S = String
 * E = DeviceContext
 * F = List of DeviceContexts
 */
public class StringConversionUtil {
    /**
     * Creates a String representation of an object that preserves the object type.
     *
     * @param o the object to convert
     *
     * @return a String
     */
    public static String createTypedValueString(Object o) {
        if (o == null) {
            return "N";
        } else if (o instanceof Integer) {
            return "I" + Integer.toString((Integer)o);
        } else if (o instanceof Double) {
            return "D" + Double.toString((Double)o);
        } else if (o instanceof Boolean) {
            return "B" + Boolean.toString((Boolean) o);
        } else if (o instanceof DeviceContext) {
            return "E" + o.toString();
        } else if (o instanceof List) {
            StringBuilder sb = new StringBuilder("F");
            List l = (List)o;
            for (int i=0; i < l.size(); i++) {
                Object o1 = l.get(i);
                if (o1 instanceof DeviceContext) {
                    sb.append(o1.toString());
                    if (i < l.size() - 1) {
                        sb.append(",");
                    }
                } else {
                    throw new HobsonRuntimeException("Only lists of DeviceContext objects are supported");
                }
            }
            return sb.toString();
        } else {
            return "S" + o.toString();
        }
    }

    /**
     * Converts a typed String representation back to an Object.
     *
     * @param s the String
     *
     * @return an Object
     */
    public static Object castTypedValueString(String s) {
        if (s != null) {
            char c = s.charAt(0);
            switch (c) {
                case 'N':
                    return null;
                case 'I':
                    return Integer.parseInt(s.substring(1));
                case 'D':
                    return Double.parseDouble(s.substring(1));
                case 'B':
                    return Boolean.parseBoolean(s.substring(1));
                case 'E':
                    return DeviceContext.create(s.substring(1));
                case 'F':
                    List<DeviceContext> list = new ArrayList<>();
                    StringTokenizer tok = new StringTokenizer(s.substring(1), ",");
                    while (tok.hasMoreElements()) {
                        list.add(DeviceContext.create(tok.nextToken()));
                    }
                    return list;
                default:
                    return s.substring(1);
            }
        } else {
            return null;
        }
    }
}
