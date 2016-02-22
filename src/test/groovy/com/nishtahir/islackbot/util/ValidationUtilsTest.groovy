package com.nishtahir.islackbot.util

import spock.lang.Specification

class ValidationUtilsTest extends Specification {
    def "IsValidUrl"() {
        expect:

        ValidationUtils.isValidUrl("http://sqlitebrowser.org/")
        ValidationUtils.isValidUrl("http://google.com")
        ValidationUtils.isValidUrl("https://www.google.com")
        ValidationUtils.isValidUrl("https://en.wikipedia.org/wiki/Regular_expression#Examples")
        ValidationUtils.isValidUrl("developer.android.com")
        ValidationUtils.isValidUrl("8.8.8.8")
        ValidationUtils.isValidUrl("192.168.0.1")

        !ValidationUtils.isValidUrl("123.123.1234")
        !ValidationUtils.isValidUrl("spaghetti...")
        !ValidationUtils.isValidUrl("Potato")
        !ValidationUtils.isValidUrl("http://google.com\\q=test_back")

    }

    def "IsValidTacoRequest"() {
        given:
        def sessionName = "linkbait"

        expect:
        ValidationUtils.isValidTacoRequest("<@linkbait>: gimme a taco", sessionName)
        ValidationUtils.isValidTacoRequest("<@linkbait>: give me a taco", sessionName)
        ValidationUtils.isValidTacoRequest("<@linkbait>: give me a taco plz", sessionName)
        ValidationUtils.isValidTacoRequest("<@linkbait>: give me a taco plzz", sessionName)
        ValidationUtils.isValidTacoRequest("<@linkbait>: give me a taco plzz!!!", sessionName)

        ValidationUtils.isValidTacoRequest("<@linkbait>: gimme a taco!", sessionName)

        !ValidationUtils.isValidTacoRequest("<@linkbait> gimme a taco", sessionName)
    }
}