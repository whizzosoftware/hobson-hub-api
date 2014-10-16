/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * A convenience class for performing various bundle related functions.
 *
 * @author Dan Noguerol
 */
public class BundleUtil {
    static public BundleContext getBundleContext(Class clazz, String bundleSymbolicName) {
        if (bundleSymbolicName == null) {
            return FrameworkUtil.getBundle(clazz).getBundleContext();
        } else {
            for (Bundle bundle : FrameworkUtil.getBundle(clazz).getBundleContext().getBundles()) {
                if (bundleSymbolicName.equalsIgnoreCase(bundle.getSymbolicName())) {
                    return bundle.getBundleContext();
                }
            }
        }
        return null;
    }

}
