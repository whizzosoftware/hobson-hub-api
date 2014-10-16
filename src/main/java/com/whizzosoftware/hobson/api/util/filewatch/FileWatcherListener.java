/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util.filewatch;

import java.io.File;

/**
 * An interface for receiving file change events.
 *
 * @author Dan Noguerol
 */
public interface FileWatcherListener {
    public void onFileChanged(File ruleFile);
}
