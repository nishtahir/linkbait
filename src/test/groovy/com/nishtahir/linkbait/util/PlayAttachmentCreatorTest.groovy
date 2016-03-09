package com.nishtahir.linkbait.util

import spock.lang.Specification

/**
 * Created by nish on 2/27/16.
 */
class PlayAttachmentCreatorTest extends Specification {
    def "GetAppDetailsFromPlayStore_withFreeApp_ReturnsExpectedOutput"() {
        given:
        String url = PlayAttachmentCreator.getUrlFromPlayId('net.broapp.app')
        Map values = PlayAttachmentCreator.getAppDetailsFromPlayStore(url)

        expect:
        values['title'] == 'BroApp'
        values['author'] == 'Factorial Products Pty. Ltd.'
        values['desc'].startsWith('BroApp is your clever relationship wingman. ' +
                'It automatically messages your girlfriend sweet things so you can spend more time with the Bros. ' +
                'Select your girlfriend\'s number, add some sweet messages, and set the time of day when you want those messages sent. ' +
                'BroApp takes care of the rest.')

        values['price'] == 'Free'
    }

    def "GetAppDetailsFromPlayStore_withPaidApp_ReturnsExpectedOutput"() {
        given:
        String url = PlayAttachmentCreator.getUrlFromPlayId('com.mojang.minecraftpe')
        Map values = PlayAttachmentCreator.getAppDetailsFromPlayStore(url)

        expect:
        values['title'] == 'Minecraft: Pocket Edition'
        values['author'] == 'Mojang'
        values['price'] == '$6.99'
    }

    def "GetPlayStoreDetailsAsSlackAttachment"() {

    }
}
