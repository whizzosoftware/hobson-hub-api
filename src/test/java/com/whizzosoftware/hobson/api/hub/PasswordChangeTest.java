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

public class PasswordChangeTest {
    @Test
    public void testIsValid() {
        assertTrue(new PasswordChange("admin", "Admin12!").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd#").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd\"").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd'").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd@").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd#").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd$").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd%").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd&").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd*").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd(").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd)").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd_").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd+").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd=").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd|").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd<").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd>").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd?").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd{").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd}").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd[").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd]").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd~").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd-").isValid());
        assertTrue(new PasswordChange("admin", "Passw0rd\\").isValid());

        // less than 8 characters
        assertFalse(new PasswordChange("admin", "a").isValid());
        assertFalse(new PasswordChange("admin", "ad").isValid());
        assertFalse(new PasswordChange("admin", "adm").isValid());
        assertFalse(new PasswordChange("admin", "admi").isValid());
        assertFalse(new PasswordChange("admin", "admin").isValid());
        assertFalse(new PasswordChange("admin", "admiN1").isValid());
        assertFalse(new PasswordChange("admin", "admiN12").isValid());
        assertTrue(new PasswordChange("admin", "admiN12!").isValid());

        // missing special character
        assertFalse(new PasswordChange("admin", "Passw0rd").isValid());
        assertTrue(new PasswordChange("admin", "Pas$w0rd").isValid());

        // missing number
        assertFalse(new PasswordChange("admin", "Pas$word").isValid());
        assertTrue(new PasswordChange("admin", "Pas$w0rd").isValid());

        // missing upper case
        assertFalse(new PasswordChange("admin", "pas$word").isValid());
        assertTrue(new PasswordChange("admin", "Pas$w0rd").isValid());
    }
}
