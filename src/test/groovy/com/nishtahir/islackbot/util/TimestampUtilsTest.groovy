package com.nishtahir.islackbot.util

import spock.lang.Specification

/**
 * Created by nish on 2/20/16.
 */
class TimestampUtilsTest extends Specification {
    def "GetStartOfDay"() {
        given:
        Calendar calendar = Calendar.instance
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfDay(calendar) == 1456005600L
    }

    def "GetStartOfWeek"() {
        given:
        Calendar calendar = Calendar.instance
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfWeek(calendar) == 1455487200L
    }

    def "GetStartOfMonth"() {
        given:
        Calendar calendar = Calendar.instance
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfMonth(calendar) == 1454277600L
    }


}
