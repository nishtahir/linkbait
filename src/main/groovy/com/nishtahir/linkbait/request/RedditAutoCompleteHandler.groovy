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
        Matcher matcher = (message =~ /(^|\s+)\/?(?<sub>(r\/\w+))\/?(\s+|$)/)
        if (!matcher.find())
            throw new RequestParseException('Not a valid subreddit')

        return matcher.group('sub')
    }

    @Override
    void handle(SlackSession session, SlackMessagePosted event) {
        try {
            String subreddit = parse(event.messageContent, session.sessionPersona().id)
            session.sendMessage(event.channel, "For the lazy... <https://www.reddit.com/${subreddit}/|https://www.reddit.com/${subreddit}/>", null)
        } catch (RequestParseException ignore) {

        }
    }
}
