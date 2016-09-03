package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.slack.SlackMessageBuilder
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by nish on 8/23/16.
 */
class SlackMessageFormatterTest extends Specification {

    @Shared
    SlackMessageBuilder formatter

    def setup() {
        formatter = new SlackMessageBuilder()
    }

    def "Should ignore null or empty strings"() {

    }

    def "Should"() {

    }

    def "Bold"() {

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
