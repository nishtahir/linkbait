package com.nishtahir.linkbait.util

import spock.lang.Specification

class SteamAttachmentCreatorTest extends Specification {

    /**
     * This test may need to be updated regularly as its content may change.
     * As of specification this
     * <a href="http://store.steampowered.com/app/292120/">link</a> should
     * point to FINAL FANTASY® XIII on the steam store.
     */
    def "GetAppDetailsFromSteam_WithValidId_ReturnsExpectedOutput"() {
        given:
        Map values = SteamAttachmentCreator.getAppDetailsFromSteam(292120)

        expect:
        values['title'] == 'FINAL FANTASY® XIII'
        values['publishers'] == 'SQUARE ENIX'
        values['imageUrl'] == 'http://cdn.akamai.steamstatic.com/steam/apps/292120/header.jpg?t=1447361145'
        values['availability'] == 'Windows'
        values['price'] == '$15.99'
    }

    def "GetAppDetailsFromSteam_WithInValidId_ReturnsNull"() {
        given:
        Map values = SteamAttachmentCreator.getAppDetailsFromSteam(999999999999) // Probably doesn't exist...

        expect:
        values == null
    }
}
