package com.nishtahir.islackbot.util

import spock.lang.Specification

/**
 * Tests to ensure that TimestampUtils works correctly.
 * To keep the build happy, all tests should be run on the
 * same timezone. GMT is preferred. Double checks can be done manually
 * here
 *
 * http://www.epochconverter.com/
 */
class TimestampUtilsTest extends Specification {

    Calendar calendar

    def setup() {
        calendar = Calendar.getInstance()
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"))
    }

    def "GetStartOfDay_WithRandomDate_ReturnsCorrectOutput"() {
        given:
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfDay(calendar) == 1456012800L
    }

    def "GetStartOfWeek_WithRandomDate_ReturnsCorrectOutput"() {
        given:
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfWeek(calendar) == 1455494400L
    }

    def "GetStartOfMonth_WithRandomDate_ReturnsCorrectOutput"() {
        given:
        calendar.setTimeInMillis(1456068025000)

        expect:
        TimestampUtils.getStartOfMonth(calendar) == 1454284800L
    }


}
