package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener

/**
 * Created by nish on 7/19/16.
 */
interface PluginContext {

    fun getPluginState()

    fun getMessenger(): Messenger

    fun getConfiguration(): Configuration

    fun registerListener(listener: EventListener)

    fun unregisterListener(listener: EventListener)
}