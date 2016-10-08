package com.nishtahir.linkbait.memegen

import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

class MemegenPlugin : LinkbaitPlugin() {

    var memegenHandler: MemegenHandler? = null

    override fun start(context: PluginContext) {
        memegenHandler = MemegenHandler(context)
        memegenHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        memegenHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }
}