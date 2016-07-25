package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.RequestHandler
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * React with a tasty snack whenever Android an android  is mentioned.
 */
class AndroidReactionHandler implements RequestHandler<String, SlackMessagePosted> {

    static final def reactions = [
            n: 'nutella',
            m: 'marshmallow',
            l: 'lollipop',
            k: 'kitkat',
            j: 'jellybean',
            i: 'ics',
            h: 'honeycomb',
            g: 'gingerbread'
    ]

    @Override
    String parse(final String message, final String sessionId) {

        def matcher = (message.toLowerCase() =~ /.*?\b(${getPatternContent()})\b.*?/)
        if (matcher.find()) {
            return matcher.group(1).charAt(0)
        }
        throw new RequestParseException("This message wasn't aimed at the bot.")
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            String result = reactions.get(parse(event.messageContent, session.sessionPersona().id))
            if (result != null) {
                session.addReactionToMessage(event.channel, event.timestamp, result)
                return true
            }
        } catch (RequestParseException ignore) {
        }
        return false
    }

/**
 *
 * @return
 */
    private String getPatternContent() {
        reactions.inject([]) { result, entry ->
            result << "$entry.key|$entry.value"
        }.join('|')
    }

}
