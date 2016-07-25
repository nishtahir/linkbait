package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.exception.RequestParseException
import spock.lang.Specification

/**
 * Created by nish on 3/12/16.
 */
class AndroidReactionHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"
        AndroidReactionHandler handler = new AndroidReactionHandler()

        expect:
        handler.parse("The situation is dire! Android N might be called Nutmeg", sessionName) == 'n'
        handler.parse("N for Nuttela!", sessionName) == 'n'
        handler.parse("Still running on lollipop", sessionName) == 'l'
        handler.parse("It came out with the L release", sessionName) == 'l'
        handler.parse("Kitkat release", sessionName) == 'k'
    }

    def "Parse_InValidInput_ThrowsRequestParseException"() {
        setup:
        def sessionName = "linkbait"

        when:
        new AndroidReactionHandler().parse("potato", sessionName)

        then:
        thrown RequestParseException
    }
}
