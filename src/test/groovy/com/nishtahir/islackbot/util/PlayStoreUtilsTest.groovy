package com.nishtahir.islackbot.util

import com.nishtahir.islackbot.App
import spock.lang.Specification

/**
 * Created by nish on 2/27/16.
 */
class PlayStoreUtilsTest extends Specification {
    def "GetAppDetailsFromPlayStore"() {
        given:
        String url = PlayStoreUtils.getUrlFromPlayId('net.broapp.app')
        Map values = PlayStoreUtils.getAppDetailsFromPlayStore(url)

        expect:
        values['title'] == 'BroApp'
        values['author'] == 'Factorial Products Pty. Ltd.'
        values['desc'].startsWith('BroApp is your clever relationship wingman. ' +
                'It automatically messages your girlfriend sweet things so you can spend more time with the Bros. ' +
                'Select your girlfriend\'s number, add some sweet messages, and set the time of day when you want those messages sent. ' +
                'BroApp takes care of the rest.')


    }

    def "GetPlayStoreDetailsAsSlackAttachment"() {

    }
}
