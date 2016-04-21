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

        expect:
        AndroidReactionHandler.instance.parse("The situation is dire! Android N might be called Nutmeg", sessionName) == null
        AndroidReactionHandler.instance.parse("N for Nuttela!", sessionName) == null
    }

    def "Parse_InValidInput_ThrowsRequestParseException"() {
        setup:
        def sessionName = "linkbait"

        when:
        AndroidReactionHandler.instance.parse("potato", sessionName)

        then:
        thrown RequestParseException
    }
}
