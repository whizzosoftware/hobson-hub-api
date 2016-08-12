/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PropertyContainerClassTest {
    @Test
    public void testEvaluatePropertyConstraintsWithNoProperties() {
        PropertyContainerClass pcc = new PropertyContainerClass(PropertyContainerClassContext.create(HubContext.createLocal(), "cc1"), "pcc", PropertyContainerClassType.DEVICE_CONFIG, null, null);
        assertTrue(pcc.evaluatePropertyConstraints(null));
    }
}