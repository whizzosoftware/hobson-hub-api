/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class UserAccountTest {
    @Test
    public void testConstructor() {
        long now = System.currentTimeMillis();
        UserAccount ua = new UserAccount("" + now, "foo1,foo2");
        assertEquals(now, (long)ua.getExpiration());
        assertEquals(2, ua.getHubs().size());
        Iterator it = ua.getHubs().iterator();
        assertEquals("foo1", it.next());
        assertEquals("foo2", it.next());
    }
}
