package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Created by nish on 9/2/16.
 */
class JobSearchLinkbaitPlugin : LinkbaitPlugin() {

    private lateinit var handler: JobSearchHandler

    override fun start(context: PluginContext) {
        handler = JobSearchHandler(context)
        handler.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        handler.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
        // Here be Nutella
    }

}

