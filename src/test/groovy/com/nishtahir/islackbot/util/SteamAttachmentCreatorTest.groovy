package com.nishtahir.islackbot.util

import spock.lang.Specification

/**
 * Created by nish on 3/5/16.
 */
class SteamAttachmentCreatorTest extends Specification {

    def "GetAppDetailsFromSteam"() {
        given:
        String url = "http://store.steampowered.com/app/292120/"
        Map values = SteamAttachmentCreator.getAppDetailsFromSteam(url)

        expect:
        values['title'] == 'FINAL FANTASY® XIII'
//        values['author'] == 'Factorial Products Pty. Ltd.'
//        values['desc'].startsWith('BroApp is your clever relationship wingman. ' +
//                'It automatically messages your girlfriend sweet things so you can spend more time with the Bros. ' +
//                'Select your girlfriend\'s number, add some sweet messages, and set the time of day when you want those messages sent. ' +
//                'BroApp takes care of the rest.')

        values['price'] == 'Free'
    }
}
