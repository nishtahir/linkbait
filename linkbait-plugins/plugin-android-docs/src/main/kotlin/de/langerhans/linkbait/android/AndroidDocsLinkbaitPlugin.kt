package de.langerhans.linkbait.android

import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

class AndroidDocsLinkbaitPlugin : LinkbaitPlugin() {

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