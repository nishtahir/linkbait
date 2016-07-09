package com.nishtahir.linkbait.plugin

import ro.fortsoft.pf4j.Plugin
import ro.fortsoft.pf4j.PluginWrapper

/**
 * Base implementation of a plugin
 */
abstract class LinkbaitPlugin(wrapper: PluginWrapper?) : Plugin(wrapper) {

    /**
     *
     */
    override fun start() {
        super.start()
    }

    /**
     *
     */
    override fun stop() {
        super.stop()
    }
}