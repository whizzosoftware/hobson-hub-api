/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.util.filewatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * A thread that watches for changes to a specific file.
 *
 * @author Dan Noguerol
 */
public class FileWatcherThread extends Thread {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WatchService watcher;
    private WatchKey watchKey;
    private File watchedFile;
    private FileWatcherListener listener;

    public FileWatcherThread(File watchedFile, FileWatcherListener listener) throws IOException {
        super();

        this.watchedFile = watchedFile;
        this.listener = listener;

        watcher = FileSystems.getDefault().newWatchService();

        String dir = watchedFile.getParentFile().getAbsolutePath();
        logger.debug("Watching directory: {}", dir);

        watchKey = Paths.get(dir).register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public void run() {
        logger.debug("Thread starting");
        while (!isInterrupted()) {
            logger.debug("Waiting for next change to file {}", watchedFile.getAbsolutePath());
            try {
                // wait for the key to be signalled
                WatchKey key = watcher.take();

                logger.debug("Detected a directory change");

                for (WatchEvent<?> event : key.pollEvents()) {
                    // get the kind of event
                    WatchEvent.Kind<?> kind = event.kind();

                    // ignore OVERFLOW events
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    // the filename is the context of the event
                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filename = ev.context();

                    // if the filename is the same as the one we're watching, alert listener
                    if (filename.toString().equals(watchedFile.getName()) && listener != null) {
                        try {
                            listener.onFileChanged(watchedFile);
                        } catch (Exception e) {
                            logger.error("Error processing changed file", e);
                        }
                    }
                }

                // reset the key
                boolean valid = key.reset();
                if (!valid) {
                    logger.error("Watch key is no longer valid; unable to continue monitoring file: {}", watchedFile.getAbsolutePath());
                    break;
                }
            } catch (InterruptedException x) {
                logger.debug("Thread was interrupted", x);
            }
        }
        logger.debug("Thread exiting");
    }
}
