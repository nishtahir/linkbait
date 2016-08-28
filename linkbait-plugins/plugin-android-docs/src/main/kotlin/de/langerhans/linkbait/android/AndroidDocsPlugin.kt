package de.langerhans.linkbait.android

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext

class AndroidDocsPlugin: Plugin() {

    private lateinit var handler: AndroidHandler

    override fun start(context: PluginContext) {
        println("AndroidDocs starting!")
        handler = AndroidHandler(context)
        handler.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        println("AndroidDocs stopping!")
        handler.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
        // Here be Nutella
    }

}