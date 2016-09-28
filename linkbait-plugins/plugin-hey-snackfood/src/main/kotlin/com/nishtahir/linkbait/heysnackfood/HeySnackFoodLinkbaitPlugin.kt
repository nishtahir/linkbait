package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext

/**
 * Pokedex plugin for Linkbait. To edit the pokedex content.
 * Edit the resource in resources/pokedex.sqlite
 *
 * Disclaimer:
 * Official pokedex resource is http://www.pokemon.com/us/pokedex/
 * All Pokémon content is © Nintendo, Game Freak, and The Pokémon Company.
 *
 */

class HeySnackFoodLinkbaitPlugin : LinkbaitPlugin() {

    /**
     *
     */
    var heySnackFoodHandler: HeySnackFoodHandler? = null

    companion object {
        init {
            Class.forName("org.sqlite.JDBC")
            //TODO - get data dir from config
            InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:data/hey_snackfood.sqlite"))
            InjektModule.scope.addFactory { HeySnackFoodService() }
        }
    }

    override fun start(context: PluginContext) {
        heySnackFoodHandler = HeySnackFoodHandler(context)
        heySnackFoodHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        heySnackFoodHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }

}


