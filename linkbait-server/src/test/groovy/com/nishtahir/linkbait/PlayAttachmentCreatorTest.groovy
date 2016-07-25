package com.nishtahir.linkbait

import spock.lang.Specification

/**
 * Probably not the best idea to do network requests here. Should fix.
 */
class PlayAttachmentCreatorTest extends Specification {
    def "GetAppDetailsFromPlayStore_withFreeApp_ReturnsExpectedOutput"() {
        given:
        String url = PlayAttachmentCreator.getUrlFromPlayId('com.netflix.mediaclient')
        Map values = PlayAttachmentCreator.getAppDetailsFromPlayStore(url)

        expect:
        values['title'] == 'Netflix'
        values['author'] == 'Netflix, Inc.'
        values['desc'].startsWith('Netflix is the world’s leading subscription service for watching TV episodes and movies on your phone. This Netflix mobile application delivers the best experience anywhere, anytime.')

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
