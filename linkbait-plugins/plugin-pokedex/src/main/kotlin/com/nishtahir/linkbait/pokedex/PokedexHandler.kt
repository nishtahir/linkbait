package com.nishtahir.linkbait.pokedex;

import com.google.common.eventbus.Subscribe
import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import uy.kohesive.injekt.Injekt


/**
 *  Handle the pokedex command
 */
class PokedexHandler(val context: PluginContext) : MessageEventListener {

    val service: PokedexService = InjektModule.scope.get()

    @Subscribe
    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot) {
            val tokens = event.message.split("""\s+""".toRegex())
            if ("pokedex" == tokens[0]) {
                val pokemon = service.findPokemon(parseTextInput(tokens[1]))
                pokemon?.let { pokemon ->
                    val attachment = Attachment(
                            title = pokemon.name.capitalize().orEmpty(),
                            body = pokemon.description.orEmpty(),
                            color = service.getColorHex(pokemon.color),
                            thumbnailUrl = pokemon.thumbnail)

                    attachment.additionalFields = mapOf(
                            "Id" to pokemon.id,
                            "Height" to "${pokemon.height / 10.0}m",
                            "Weight" to "${pokemon.weight / 10.0}kg",
                            "Abilities" to pokemon.getAbilities().joinToString(),
                            "Type" to pokemon.getType().joinToString()
                    )

                    context.getMessenger().sendAttachment(event.channel, attachment);
                }
            }
        }
    }

    fun parseTextInput(input: String): Any {
        if (input.matches("""\d+""".toRegex())) {
            return input.toInt()
        }
        return input
    }

}