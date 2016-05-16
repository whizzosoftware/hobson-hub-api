/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class HobsonUserTest {
    @Test
    public void testBuilder() {
        long now = System.currentTimeMillis();
        HobsonUser u = new HobsonUser.Builder("uid")
            .email("email")
            .givenName("name1")
            .familyName("name2")
            .account(new UserAccount(now, Collections.singletonList("hub1")))
            .build();
        assertEquals("uid", u.getId());
        assertEquals("email", u.getEmail());
        assertEquals("name1", u.getGivenName());
        assertEquals("name2", u.getFamilyName());
        assertNotNull(u.getAccount());
        assertEquals(now, (long)u.getAccount().getExpiration());
        assertTrue(u.getAccount().hasAvailableHubs());
    }
}
