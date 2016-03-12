package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import spock.lang.Specification

/**
 * Created by nish on 3/12/16.
 */
class NReactionHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"

        expect:
        NReactionHandler.instance.parse("The situation is dire! Android N might be called Nutmeg", sessionName) == null
        NReactionHandler.instance.parse("N for Nuttela!", sessionName) == null
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
