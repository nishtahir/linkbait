package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext
import org.apache.commons.io.FileUtils
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import java.io.File
import java.io.InputStream

/**
 * Pokedex plugin for Linkbait. To edit the pokedex content.
 * Edit the resource in resources/pokedex.sqlite
 *
 * Disclaimer:
 * Official pokedex resource is http://www.pokemon.com/us/pokedex/
 * All Pokémon content is © Nintendo, Game Freak, and The Pokémon Company.
 *
 */
class PokedexPlugin : Plugin() {

    /**
     *
     */
    var pokedexHandler: PokedexHandler? = null

    companion object : InjektMain() {

        init {
            Class.forName("org.sqlite.JDBC")
        }

        override fun InjektRegistrar.registerInjectables() {
            val dex: File = copyInputStreamToTempFile(
                    PokedexPlugin::class.java.classLoader.getResourceAsStream("linkbait-pokedex.sqlite"))
            addSingleton(JdbcConnectionSource("jdbc:sqlite:${dex.absolutePath}"))
        }

        fun copyInputStreamToTempFile(inputStream: InputStream): File {
            val tempFile: File = File.createTempFile("database", "sqlite");
            tempFile.deleteOnExit();
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            return tempFile
        }
    }

    override fun start(context: PluginContext) {
        println("start")
        pokedexHandler = PokedexHandler(context)
        pokedexHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        println("stop")
        pokedexHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }

}


