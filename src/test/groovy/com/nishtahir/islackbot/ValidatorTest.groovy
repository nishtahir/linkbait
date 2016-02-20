package com.nishtahir.islackbot

import spock.lang.Specification

class ValidatorTest extends Specification {
    def "IsValidUrl"() {
        expect:
        Validator.isValidUrl("http://sqlitebrowser.org/")
        Validator.isValidUrl("http://google.com")
        Validator.isValidUrl("https://www.google.com")
        !Validator.isValidUrl("123.123.1234")
        !Validator.isValidUrl("Potato")
        !Validator.isValidUrl("http://google.com\\q=test_back")

    }

    def "IsValidTacoRequest"() {
        given:
            def sessionName = "linkbait"

        expect:
        Validator.isValidTacoRequest("@linkbait: gimme a taco", sessionName)
        !Validator.isValidTacoRequest("@linkbait gimme a taco", sessionName)
    }
}