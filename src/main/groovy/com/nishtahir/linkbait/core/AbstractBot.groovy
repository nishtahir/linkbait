package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.event.Messenger

/**
 * Base implementation for a bot
 */
abstract class AbstractBot {

    /**
     * Bot owner
     */
    String owner

    /**
     * Associated event bus
     */
    protected EventBus eventBus

    /**
     * Starts a slack session
     */
    abstract void start();

    /**
     * Stops the slack session
     */
    abstract void stop();

    /**
     * Message passer
     * @return
     */
    abstract Messenger getMessenger();
}
