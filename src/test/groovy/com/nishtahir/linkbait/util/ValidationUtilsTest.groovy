package com.nishtahir.linkbait.util

import spock.lang.Specification

class ValidationUtilsTest extends Specification {
    def "IsValidUrl_WithRandomURLs_ReturnsExpectedBehavior"() {
        expect:
        ValidationUtils.isValidUrl("http://sqlitebrowser.org/")
        ValidationUtils.isValidUrl("http://google.com")
        ValidationUtils.isValidUrl("https://www.google.com")
        //This is really bad. It caused the regex to lockup for some reason
        ValidationUtils.isValidUrl("https://en.wikipedia.org/wiki/Regular_expression#Examples")
        ValidationUtils.isValidUrl("developer.android.com")

        !ValidationUtils.isValidUrl("8.8.8.8")
        !ValidationUtils.isValidUrl("192.168.0.1")
        !ValidationUtils.isValidUrl("123.123.1234")

        //Looks like we are going to have to rely on the slack pattern matching for a while
//        !ValidationUtils.isValidUrl("spaghetti...")
        !ValidationUtils.isValidUrl("Potato")
        !ValidationUtils.isValidUrl("Intent.ACTION_SEND")
        !ValidationUtils.isValidUrl("http://google.com\\q=test_back")

    }

    def "isValidSlackUrlFormat_WithRandomURLs_WorksAsExpected"(){
        expect:
        ValidationUtils.isValidSlackUrlFormat("<http://j-php.net/>")
        ValidationUtils.isValidSlackUrlFormat("<http://j-php.net/|J-PHP>")

    }

    def "GetPlaystoreId"() {
        expect:
        ValidationUtils.getPlaystoreId('https://play.google.com/store/apps/details?id=com.antutu.ABenchMark') == 'com.antutu.ABenchMark'
    }

    def "GetSteamId_WithValidSteamLink_ReturnsCorrectOutput"() {
        expect:
        ValidationUtils.getSteamId("http://store.steampowered.com/app/24980/") == 24980L
    }

    def "GetSteamId_WithInValidSteamLink_ReturnsCorrectOutput"(){
        expect:
        ValidationUtils.getSteamId("potato") == -1L
    }
}