package com.nishtahir.linkbait.core

/**
 * Created by nish on 6/23/16.
 */
abstract class AbstractBot {

    /**
     * Starts a slack session
     */
    abstract void start();

    /**
     * Stops the slack session
     */
    abstract void stop();
}
