package com.nishtahir.linkbait.core

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by nish on 8/23/16.
 */
class SlackMessageFormatterTest extends Specification {

    @Shared
    SlackMessageFormatter formatter

    def setup() {
        formatter = new SlackMessageFormatter()
    }

    def "Should ignore null or empty strings"() {
        formatter.format {

        }
    }

    def "Should"() {

    }

    def "Bold"() {
        given:
        String message = formatter.format {
            par "And this gives you "
            bold "power"
            par " over me?"
        }

        expect:
        message == "And this gives you *power* over me?"
    }

    def "Italics"() {

    }

    def "Strike"() {

    }

    def "Pre"() {

    }

    def "Code"() {

    }

    def "Emoji"() {

    }

    def "Link"() {

    }
}
