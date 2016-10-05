package co.jasonwyatt.linkbait.asciitext

import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Created by jason on 8/22/16.
 */
class AsciiTextPlugin : LinkbaitPlugin() {

    private lateinit var handler: AsciiTextHandler

    override fun start(context: PluginContext) {
        handler = AsciiTextHandler(context)
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
