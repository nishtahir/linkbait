package com.nishtahir.linkbait.plugin

/**
 * Base implementation of a plugin
 */
abstract class LinkbaitPlugin : Plugin{

    /**
     * @param context
     */
    abstract override fun start(context: PluginContext)

    /**
     *  @param context
     */
    abstract override fun stop(context: PluginContext)

    /**
     *
     */
    abstract override fun onPluginStateChanged()
}

interface Plugin {
    fun start(context: PluginContext)

    /**
     *  @param context
     */
    fun stop(context: PluginContext)

    /**
     *
     */
    fun onPluginStateChanged()
}