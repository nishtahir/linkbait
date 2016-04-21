package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import spock.lang.Specification

class TacoRequestHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"

        expect:
        TacoRequestHandler.instance.parse("<@${sessionName}>: give me a taco", sessionName)[0] == 'me'
        TacoRequestHandler.instance.parse("<@${sessionName}>: give <@tim> a taco", sessionName)[0] == '<@tim>'
    }

    def "Parse_InValidInput_ThrowsRequestParseException"() {
        setup:
        def sessionName = "linkbait"

        when:
        TacoRequestHandler.instance.parse("potato", sessionName)[0] == 'me'

        then:
        thrown RequestParseException
    }
}
