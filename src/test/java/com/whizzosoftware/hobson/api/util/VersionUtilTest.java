/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class VersionUtilTest {
    @Test
    public void testVersionCompare() {
        assertEquals(0, VersionUtil.versionCompare("1.0.0", "1.0.0"));
        assertEquals(1, VersionUtil.versionCompare("1.1.0", "1.0.0"));
        assertEquals(-1, VersionUtil.versionCompare("1.0.0", "1.1.0"));
        assertEquals(-1, VersionUtil.versionCompare(null, "1.1.0"));
        assertEquals(1, VersionUtil.versionCompare("1.1.0", null));
        assertEquals(-1, VersionUtil.versionCompare("0.1.0", "0.1.1"));
    }
}
