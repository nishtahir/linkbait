package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.AbstractExecutionThreadService
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.PluginContext
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import groovy.util.logging.Slf4j
import org.jetbrains.annotations.NotNull

/**
 * Base implementation for a bot
 */
@Slf4j
abstract class AbstractBot extends AbstractExecutionThreadService implements PluginContext {

    /**
     * @deprecated
     */
    @NotNull
    Configuration configuration

    /**
     * Messenger used to send stuff to the message service
     */
    @NotNull
    abstract Messenger messenger

    /**
     * Bot id
     */
    @NotNull
    String id

    /**
     *
     */
    @NotNull
    PluginLoader loader

    /**
     * Associated event bus
     */
    @NotNull
    protected EventBus eventBus

    /**
     *
     * @param pluginRepository
     * @param pluginDirectory
     */
    AbstractBot(@NotNull File pluginRepository, @NotNull File pluginDirectory) {
        loader = new PluginLoader(pluginRepository)
        loader.loadPlugins(pluginDirectory)
    }

    /**
     * @param configuration
     * @deprecated
     */
    AbstractBot(@NotNull Configuration configuration) {
        this(configuration.pluginRepository, configuration.pluginDirectory)
        this.configuration = configuration
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void startUp() throws Exception {
        loader.plugins.each { name, plugin ->
            log.info("Starting plugin: ${name}")
            plugin.start(this)
        }
    }

    @Override
    protected void run() throws Exception {

    }

    @Override
    protected void shutDown() throws Exception {
        loader.plugins.each { name, plugin ->
            log.info("Stoping plugin: ${name}")
            plugin.stop(this)
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
