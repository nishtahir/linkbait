package com.nishtahir.islackbot

import spock.lang.Specification

class ValidatorTest extends Specification {
    def "IsValidUrl"() {
        expect:
            Validator.isValidUrl("http://sqlitebrowser.org/")
            Validator.isValidUrl("http://google.com")
            Validator.isValidUrl("https://www.google.com")
            !Validator.isValidUrl("Potato")

    }
}
