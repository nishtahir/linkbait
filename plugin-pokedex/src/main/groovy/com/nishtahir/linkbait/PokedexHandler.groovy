package com.nishtahir.linkbait

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.MessageRequestHandler
import com.ullink.slack.simpleslackapi.SlackAttachment
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import groovy.transform.Canonical
import org.apache.commons.io.FileUtils

/**
 *
 */
@Canonical
class PokedexHandler extends MessageRequestHandler {

    /**
     *
     */
    private static final String database = 'pokedex.sqlite'

    /**
     *
     */
    PokemonService pokemonService

    /**
     *
     */
    ConnectionSource connectionSource

    PokedexHandler() {
        Class.forName("org.sqlite.JDBC")
        File dex = copyInputStreamToTempFile(getClass().getClassLoader().getResourceAsStream(database))
        if (dex.exists()) {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:${dex.absolutePath}")
            pokemonService = new PokemonService(connectionSource)
        } else {
            throw new FileNotFoundException("Could not find $database")
        }
    }

    @Override
    String parse(final String message, final String sessionId) {
        super.parse(message, sessionId)
        def matcher = message =~ /pokedex(:?\s*)(((\s*,?\s*):?(\w+):?)*)/
        if (matcher.find()) {
            return matcher.group(2).replaceAll(':',"")
        }
        throw new RequestParseException("This message wasn't aimed at the bot.")
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        String matches = parse(event.messageContent, session.sessionPersona().id)
        matches.split(',').each { name ->
            Pokemon pokemon = pokemonService.findPokemon(name)
            if (pokemon != null) {
                session.sendMessage(event.channel, "", createPokemonAttachment(pokemon))
            }
        }
        return true
    }

    /**
     *
     * @param pokemon
     * @return
     */
    private SlackAttachment createPokemonAttachment(Pokemon pokemon) {
        SlackAttachment attachment = new SlackAttachment()
        attachment.fallback = pokemon.name.capitalize() // Yet another reason to love Groovy
        attachment.title = pokemon.name.capitalize()
        attachment.text =  pokemon.description
        attachment.addField("No", pokemon.id, true)
        attachment.addField("Type", String.join(',', [pokemon.type0.trim(), pokemon.type1.trim()]), true)
        attachment.thumb_url = pokemon.thumbnail
        return attachment
    }


    private static File copyInputStreamToTempFile(InputStream inputStream) throws IOException {
        final File tempFile = File.createTempFile('database', 'sqlite');
        tempFile.deleteOnExit();

        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile
    }
    
}
