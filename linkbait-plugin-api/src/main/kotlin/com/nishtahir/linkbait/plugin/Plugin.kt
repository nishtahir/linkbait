package com.nishtahir.linkbait.plugin

/**
 * Base implementation of a plugin
 */
abstract class Plugin {

    /**
     * @param context
     */
    abstract fun start(context: PluginContext)

    /**
     *  @param context
     */
    abstract fun stop(context: PluginContext)

    /**
     *
     */
    abstract fun onPluginStateChanged()
}