/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin.http;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class URLEncoderUtilTest {
    @Test
    public void testFormat() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("p1", "val1");
        assertEquals("p1=val1", URLEncoderUtil.createQueryString(map, URLEncoderUtil.DEFAULT_ENCODING));

        map.put("p2", "val2");
        String s = URLEncoderUtil.createQueryString(map, URLEncoderUtil.DEFAULT_ENCODING);
        assertTrue("p1=val1&p2=val2".equals(s) || "p2=val2&p1=val1".equals(s));
    }
}
