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
        !ValidationUtils.isValidUrl("Intent.ACTION_SEND")
        !ValidationUtils.isValidUrl("http://google.com\\q=test_back")

    }

    def "GetPlaystoreId"(){
        expect:
        ValidationUtils.getPlaystoreId('https://play.google.com/store/apps/details?id=com.antutu.ABenchMark') == 'com.antutu.ABenchMark'
    }
}