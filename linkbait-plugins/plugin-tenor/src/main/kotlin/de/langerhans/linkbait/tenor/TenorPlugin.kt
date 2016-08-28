package de.langerhans.linkbait.tenor

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Created by maxke on 24.08.2016.
 * Better gifs!
 */
class TenorPlugin: Plugin() {

    private lateinit var handler: TenorHandler

    override fun start(context: PluginContext) {
        handler = TenorHandler(context)
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