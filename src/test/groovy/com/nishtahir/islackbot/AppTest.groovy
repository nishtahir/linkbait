package com.nishtahir.islackbot

import spock.lang.Specification

/**
 * Created by nish on 2/22/16.
 */
class AppTest extends Specification {
    def "GetAppDetailsFromPlayStore"() {
        given:
        String id = 'net.broapp.app'

        expect:
        App.getAppDetailsFromPlayStore(id)
    }
}
