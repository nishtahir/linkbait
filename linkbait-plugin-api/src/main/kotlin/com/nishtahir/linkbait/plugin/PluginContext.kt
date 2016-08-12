package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener

/**
 * Context in which plugin is currently operating. Supplied by active bot.
 */
interface PluginContext {

    /**
     * State in which plugin is operating. In case of Async operations,
     * Check the running state of the plugin before performing actions.
     */
    fun getPluginState()

    /**
     * @return Current Messenger context for sending data.
     */
    fun getMessenger(): Messenger

    /**
     * @return Bot configuration.
     */
    fun getConfiguration(): Configuration

    /**
     * @param listener Event listener to register.
     */
    fun registerListener(listener: EventListener)

    /**
     * @param listener Event listener to unregister.
     */
    fun unregisterListener(listener: EventListener)
}