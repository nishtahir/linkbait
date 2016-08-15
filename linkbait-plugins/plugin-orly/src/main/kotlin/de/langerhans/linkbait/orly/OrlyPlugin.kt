package de.langerhans.linkbait.orly

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Created by maxke on 15.08.2016.
 * O RLY?
 */
class OrlyPlugin: Plugin() {

    private lateinit var handler: OrlyHandler

    override fun start(context: PluginContext) {
        handler = OrlyHandler(context)
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