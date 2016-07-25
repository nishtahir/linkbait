package com.nishtahir.linkbait.core.util

import spock.lang.Specification

/**
 * Created by nish on 6/23/16.
 */
class JSONUtilsTest extends Specification {
    def "JsonToPlugin"() {
        given:
            String test = ""

        expect:
            test.equals("value")
    }
}
