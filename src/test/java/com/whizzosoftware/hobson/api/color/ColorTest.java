/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.color;

import org.junit.Test;
import static org.junit.Assert.*;

public class ColorTest {
    @Test
    public void testStringConstructor() {
        Color c;

        c = new Color("hsb(360,100,100)");
        assertEquals(360, c.getHue());
        assertEquals(100, c.getSaturation());
        assertEquals(100, c.getBrightness());
        assertNull(c.getColorTemperature());

        c = new Color("kb(2500,100)");
        assertEquals(2500, (int)c.getColorTemperature());
        assertEquals(100, c.getBrightness());

        try {
            new Color("rgb(1,2,3)");
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Color("hsb(1,2)");
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Color("hsb(a,b,c)");
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToString() {
        Color c = new Color(360, 100, 100);
        assertEquals("hsb(360,100,100)", c.toString());

        c = new Color(2500, 100);
        assertEquals("kb(2500,100)", c.toString());
    }
}
