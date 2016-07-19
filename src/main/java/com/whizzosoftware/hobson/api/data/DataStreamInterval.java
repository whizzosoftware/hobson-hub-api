/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.data;

/**
 * An interval indicating effectivity of data stream data.
 *
 * @author Dan Noguerol
 */
public enum DataStreamInterval {
    HOURS_1,
    HOURS_4,
    HOURS_24,
    DAYS_7,
    DAYS_30
}
