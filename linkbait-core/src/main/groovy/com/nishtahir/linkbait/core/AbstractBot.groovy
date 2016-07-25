package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.AbstractExecutionThreadService
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.Messenger
import ro.fortsoft.pf4j.ExtensionPoint
import ro.fortsoft.pf4j.PluginManager
/**
 * Base implementation for a bot
 */
abstract class AbstractBot extends AbstractExecutionThreadService {

    /**
     * Bot owner
     */
    String owner

    /**
     * Associated event bus
     */
    protected EventBus eventBus

    /**
     *
     */
    PluginManager pluginManager;

    /**
     *
     * @param pluginManager
     * @param configuration
     */
    AbstractBot(PluginManager pluginManager) {
        this.pluginManager = pluginManager
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void startUp() throws Exception {
        super.startUp()
        pluginManager.loadPlugins()
    }

    @Override
    protected void run() throws Exception {
        pluginManager.startPlugins()
        List<ExtensionPoint> messageEventListeners = pluginManager.getExtensions(MessageEventListener.class);
        messageEventListeners.each {
            registerHandler(it)
        }

    }

    @Override
    protected void shutDown() throws Exception {
        super.shutDown()
        pluginManager.stopPlugins()
        List<MessageEventListener> messageEventListeners = pluginManager.getExtensions(MessageEventListener.class);
        messageEventListeners.each {
            unregisterHandler(it)
        }
    }

    /**
     * Registers to event bus
     * @param handler
     */
    public void registerHandler(Object handler) {
        eventBus.register(handler)
    }

    /**
     * unregisters from event bus
     * @param handler
     */
    public void unregisterHandler(Object handler) {
        eventBus.unregister(handler)
    }

    /**
     * Message passer
     * @return
     */
    abstract Messenger getMessenger();
}
