package com.nishtahir.islackbot.util

import spock.lang.Specification

class TacoUtilsTest extends Specification {

    def "ParseTacoRequest_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"

        expect:
        TacoUtils.parseTacoRequest("<@${sessionName}>: give me a taco", sessionName) == 'me'
        TacoUtils.parseTacoRequest("<@${sessionName}>: give <@tim> a taco", sessionName) == '<@tim>'
    }

    def "ParseTacoRequest_WithInValidInput_ReturnsCorrectOutput"() {
        given:
        def sessionName = "linkbait"

        expect:
        TacoUtils.parseTacoRequest("potato", sessionName) == null
    }
}
