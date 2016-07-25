package com.nishtahir.linkbait.pokedex;

import com.google.common.eventbus.Subscribe
import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext


/**
 *  Handle the pokedex command
 */
class PokedexHandler(val context: PluginContext) : MessageEventListener {

    @Subscribe
    override fun handleMessageEvent(event: MessageEvent) {
        val pokemon = findPokemon(event.message)
        pokemon?.let { pokemon ->
            val attachment = Attachment(
                    title = pokemon.name.capitalize().orEmpty(),
                    body = pokemon.description.orEmpty(),
                    color = getColor(pokemon.color),
                    thumbnailUrl = pokemon.thumbnail)

            attachment.additionalFields = mapOf(
                    "Id" to pokemon.id,
                    "Height" to pokemon.height.toString(),
                    "Weight" to pokemon.weight.toString(),
                    "Abilities" to pokemon.getAbilities().joinToString(),
                    "Type" to pokemon.getType().joinToString()
            )

            context.getMessenger().sendAttachment(event.channel, attachment);
        }

    }

}