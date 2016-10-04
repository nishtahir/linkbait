package de.langerhans.linkbait.tags

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Created by maxke on 16.08.2016.
 * Main entry for the tags plugin
 */
class TagsPlugin : LinkbaitPlugin() {

    private lateinit var handler: TagsHandler

    override fun start(context: PluginContext) {
        InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:data/tags.sqlite"))
        InjektModule.scope.addFactory { TagService() }

        handler = TagsHandler(context)
        context.registerListener(handler)
    }

    override fun stop(context: PluginContext) {
        context.unregisterListener(handler)
    }

    override fun onPluginStateChanged() {
        // Here be Nutella
    }

}