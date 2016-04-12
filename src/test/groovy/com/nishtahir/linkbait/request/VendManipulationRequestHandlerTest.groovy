package com.nishtahir.linkbait.request

import spock.lang.Specification

class VendManipulationRequestHandlerTest extends Specification {
    def "Parse_WithValidInput_ReturnsCorrectResults"() {
        given:
        def sessionName = "linkbait"

        expect:
        VendManipulationRequestHandler.instance.parse("<@${sessionName}>: vend add a Nexus 6", sessionName)[0] == "add"
        VendManipulationRequestHandler.instance.parse("<@${sessionName}>: vend remove a Nexus 6", sessionName)[0] == "remove"
        VendManipulationRequestHandler.instance.parse("<@${sessionName}>: vend add a Nexus 6", sessionName)[1] == "a Nexus 6"
    }
}
