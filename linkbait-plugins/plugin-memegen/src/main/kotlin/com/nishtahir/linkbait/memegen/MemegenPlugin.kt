package com.nishtahir.linkbait.memegen

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext


class MemegenPlugin : Plugin() {

    var memegenHandler: MemegenHandler? = null

    override fun start(context: PluginContext) {
        println("start")
        memegenHandler = MemegenHandler(context)
        memegenHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        println("stop")
        memegenHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }

}

