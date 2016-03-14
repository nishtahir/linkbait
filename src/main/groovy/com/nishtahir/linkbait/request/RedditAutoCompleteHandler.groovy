package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

import java.util.regex.Matcher

/**
 * Turn reddit references into links
 */
@Singleton
class RedditAutoCompleteHandler implements RequestHandler<String, SlackMessagePosted> {

    private static final String ANDROID7 = 'nutella'

    @Override
    String parse(String message, String sessionId) {
        Matcher matcher = (message =~ /\b(r\\/\w+)/)
        if (!matcher.find())
            throw new RequestParseException("Not a valid subreddit")

        return matcher.group()
    }

    @Override
    void handle(SlackSession session, SlackMessagePosted event) {
        try {
            String subreddit = parse(event.messageContent, session.sessionPersona().id)
            session.sendMessageOverWebSocket(event.channel, "For the lazy, <https://www.reddit.com/${subreddit}/>", null)
        } catch (RequestParseException ignore) {

        }
    }
}
