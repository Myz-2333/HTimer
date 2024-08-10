package com.zdf.worker.boot;

public interface Launch {
    /**
     * Start the server.
     */
    int start();

    /**
     * Destroy the server.
     */
    int destroy();

    int init();

}
