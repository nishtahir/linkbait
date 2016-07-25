package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.exception.RequestParseException
import spock.lang.Specification

/**
 * Created by nish on 3/12/16.
 */
class PokedexHandlerTest extends Specification {
    def "Query_Charmander_Returns_Correct_ID"() {
        given:
        PokedexHandler handler = new PokedexHandler()

        expect:
        handler.pokemonService.findPokemon("charmander").id == '4'
    }

    def "Query_WithPrefix_ReturnsCorrectResult"() {
        given:
        PokedexHandler handler = new PokedexHandler()

        expect:
        handler.pokemonService.findPokemon("charm").id == '4'
    }

    def "Query_WithSuffix_ReturnsCorrectResult"() {
        given:
        PokedexHandler handler = new PokedexHandler()

        expect:
        handler.pokemonService.findPokemon("mander").id == '4'
    }

    def "Query_WithTypo_ReturnsMostLikelyResult"() {
        given:
        PokedexHandler handler = new PokedexHandler()

        expect:
        handler.pokemonService.findPokemon("chramadner").id == '4'
    }

    def "Parse_WithValidInput_ReturnsCorrectResult"() {
        given:
        PokedexHandler handler = new PokedexHandler()
        def sessionId = "linkbait"
        def names = ['jolteon', 'jolteon, flareon, vaporeon', 'jolteon flareon vaporeon']

        expect:
        names.each { set ->
            handler.parse("<@${sessionId}>: pokedex $set", sessionId) == set
        }
    }

    def "Parse_WithInvalidSessionId_ThrowsException"() {
        given:
        PokedexHandler handler = new PokedexHandler()
        def sessionId = "linkbait"

        when:
        handler.parse("<@potato>: pokedex", sessionId) == null

        then:
        thrown RequestParseException
    }

    def "Parse_WithInvalidCommand_ThrowsException"() {
        given:
        PokedexHandler handler = new PokedexHandler()
        def sessionId = "linkbait"

        when:
        handler.parse("<@potato>: potato", sessionId) == null

        then:
        thrown RequestParseException
    }

    def "Parse_WithInvalidInput_ThrowsException"() {
        given:
        PokedexHandler handler = new PokedexHandler()
        def sessionId = "linkbait"

        when:
        handler.parse("<@potato>: pokedex 42", sessionId) == null

        then:
        thrown RequestParseException
    }
}
