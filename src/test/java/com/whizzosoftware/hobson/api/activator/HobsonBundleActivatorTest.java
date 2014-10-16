/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.activator;

import org.junit.Assert;
import org.junit.Test;

public class HobsonBundleActivatorTest {
    @Test
    public void testGetHobsonPluginClass() {
        HobsonBundleActivator a = new HobsonBundleActivator();
        Assert.assertEquals("com.whizzosoftware.hobson.openweathermap.OpenWeatherMapPlugin", a.getHobsonPluginClass("Provide-Capability: hobson.plugin=com.whizzosoftware.hobson.openweathermap.OpenWeatherMapPlugin"));
    }
}
