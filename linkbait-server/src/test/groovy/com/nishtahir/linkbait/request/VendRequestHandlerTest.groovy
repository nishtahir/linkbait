package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import spock.lang.Specification

class VendRequestHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults" () {
        given:
        def sessionName = "linkbait"

        expect:
        VendRequestHandler.instance.parse("<@${sessionName}>: vend", sessionName)[0] == 'vend'
        VendRequestHandler.instance.parse("<@${sessionName}>: send itself", sessionName)[1] == 'itself'
    }

    def "Parse_InValidInput_ThrowsRequestParseException"() {
        setup:
        def sessionName = "linkbait"

        when:
        VendRequestHandler.instance.parse("potato", sessionName)[0] == 'vend'

        then:
        thrown RequestParseException
    }
}
