/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a list of plugins.
 *
 * @author Dan Noguerol
 */
public class PluginList {
    private List<PluginDescriptor> plugins;
    private int updatesAvailable = 0;

    public PluginList(Collection<PluginDescriptor> plugins) {
        if (plugins != null) {
            this.plugins = new ArrayList<>(plugins);
            Collections.sort(this.plugins);
        } else {
            this.plugins = new ArrayList<>();
        }

        // determine how many bundles have updates available
        for (PluginDescriptor bd : this.plugins) {
            if (bd.isUpdate()) {
                updatesAvailable++;
            }
        }
    }

    public int size() {
        return plugins.size();
    }

    public List<PluginDescriptor> getPlugins() {
        return plugins;
    }

    public int getUpdatesAvailable() {
        return updatesAvailable;
    }
}
