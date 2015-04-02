/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import org.junit.Test;
import static org.junit.Assert.*;

public class LineRangeTest {
    @Test
    public void testRangeStringConstructor() {
        LineRange lr = new LineRange("lines=0-50");
        assertEquals(0, (long)lr.getStartLine());
        assertEquals(50, (long)lr.getEndLine());

        lr = new LineRange("lines=-25");
        assertNull(lr.getStartLine());
        assertEquals(25, (long)lr.getEndLine());

        lr = new LineRange("lines=10-");
        assertEquals(10, (long)lr.getStartLine());
        assertNull(lr.getEndLine());

        try {
            new LineRange("bytes=0-100");
            fail("Should have thrown illegal argument exception");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToString() {
        assertEquals("lines=0-5/*", new LineRange((long)0, (long)5).toString());
        assertEquals("lines=10-/*", new LineRange((long)10, null).toString());
        assertEquals("lines=-25/*", new LineRange(null, (long)25).toString());
    }
}
