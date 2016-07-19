package com.nishtahir.linkbait.plugin

/**
 * Base implementation of a plugin
 */
abstract class LinkbaitPlugin {

    /**
     *
     */
    fun start(context: PluginContext) {

    }

    /**
     *
     */
    fun stop(context: PluginContext) {

    }

    /**
     *
     */
    fun onPluginStateChanged(){

    }
}