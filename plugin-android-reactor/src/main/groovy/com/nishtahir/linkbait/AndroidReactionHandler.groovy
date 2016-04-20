package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.RequestHandler
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * React with a tasty snack whenever Android N is mentioned.
 */
@Singleton
class AndroidReactionHandler implements RequestHandler<Void, SlackMessagePosted> {

    private static final String ANDROID7 = 'nutella'

    @Override
    Void parse(String message, String sessionId) {
        def matcher = (message =~ /.*?\bN\b.*?/)

        if (!matcher.find())
            throw new RequestParseException("This message wasn't aimed at the bot.")
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            parse(event.messageContent, session.sessionPersona().id)
            session.addReactionToMessage(event.channel, event.timestamp, ANDROID7)
            return true
        } catch (RequestParseException ignore) {
            return false
        }
    }
}
