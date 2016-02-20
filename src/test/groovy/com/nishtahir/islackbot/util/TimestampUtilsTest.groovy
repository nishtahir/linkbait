package com.nishtahir.islackbot.util

import spock.lang.Specification

/**
 * Created by nish on 2/20/16.
 */
class TimestampUtilsTest extends Specification {
    def "GetStartOfDayToday"() {
        expect:
        println(TimestampUtils.startOfDayToday) != 0
    }
}
