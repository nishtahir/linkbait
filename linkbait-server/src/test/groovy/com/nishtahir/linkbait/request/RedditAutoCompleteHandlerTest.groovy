package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import spock.lang.Specification

class RedditAutoCompleteHandlerTest extends Specification {
    def "Parse_WithMessagesContainingSubreddit_MatchesCorrectly"() {
        given:
        def test1 = "This is a message referencing r/androiddev"
        def test2 = "This is a message referencing r/science"

        expect:
        RedditAutoCompleteHandler.instance.parse(test1, null) == "r/androiddev"
        RedditAutoCompleteHandler.instance.parse(test2, null) == "r/science"
    }

    def "Parse_WithMessagesWithoutSubreddit_ThrowException"() {
        setup:
        def context = "This is a message referencingr/androiddev"

        when:
        RedditAutoCompleteHandler.instance.parse(context, null)

        then:
        thrown RequestParseException

    }
}
