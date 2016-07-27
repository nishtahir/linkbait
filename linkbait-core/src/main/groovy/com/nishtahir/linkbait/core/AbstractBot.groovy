package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.AbstractExecutionThreadService
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.PluginContext
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import org.jetbrains.annotations.NotNull

/**
 * Base implementation for a bot
 */
abstract class AbstractBot extends AbstractExecutionThreadService implements PluginContext {

    /**
     *
     */
    Configuration configuration

    /**
     *
     */
    abstract Messenger messenger

    /**
     * Bot owner
     */
    String owner

    /**
     *
     */
    PluginLoader loader

    /**
     * Associated event bus
     */
    protected EventBus eventBus

    /**
     *
     * @param configuration
     */
    AbstractBot(@NotNull Configuration configuration) {
        this.configuration = configuration
        loader = new PluginLoader(configuration)
        loader.loadPluginsFromJar(configuration.getPluginDirectory())
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void startUp() throws Exception {
        loader.handlers.each {
            it.start(this)
        }
    }

    @Override
    protected void run() throws Exception {

    }

    @Override
    protected void shutDown() throws Exception {
        loader.handlers.each {
            it.stop(this)
        }
    }

    /**
     * Registers to event bus
     * @param handler
     */
    @Override
    void registerListener(@NotNull EventListener listener) {
        eventBus.register(listener)
    }

    /**
     * unregisters from event bus
     * @param handler
     */
    @Override
    void unregisterListener(@NotNull EventListener listener) {
        eventBus.unregister(listener)
    }
}
