package com.nishtahir.islackbot.util

import spock.lang.Specification

/**
 *
 */
class TacoUtilsTest extends Specification {
    def "ParseTacoRequest"() {
        given:
        def sessionName = "linkbait"

        expect:
        'me' == TacoUtils.parseTacoRequest("<@linkbait>: give me a taco", sessionName)
        'tim' == TacoUtils.parseTacoRequest("<@linkbait>: give tim a taco", sessionName)

    }
}
