package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import spock.lang.Specification

class HelpRequestHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"

        expect:
        HelpRequestHandler.instance.parse("<@${sessionName}>: help", sessionName) == null
        HelpRequestHandler.instance.parse("<@${sessionName}>: help me", sessionName) == null
    }

    def "Parse_InValidInput_ThrowsRequestParseException"() {
        setup:
        def sessionName = "linkbait"

        when:
        HelpRequestHandler.instance.parse("potato", sessionName)

        then:
        thrown RequestParseException
    }
}
