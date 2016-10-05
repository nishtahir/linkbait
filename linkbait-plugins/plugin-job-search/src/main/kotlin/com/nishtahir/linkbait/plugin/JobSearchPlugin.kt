package com.nishtahir.linkbait.plugin

class JobSearchPlugin : LinkbaitPlugin() {

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

