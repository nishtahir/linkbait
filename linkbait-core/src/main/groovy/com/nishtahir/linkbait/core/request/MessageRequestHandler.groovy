package com.nishtahir.linkbait.core.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * {@link RequestHandler} subclass specifically targeted to the bot in a message.
 */
@Deprecated
abstract class MessageRequestHandler implements RequestHandler<String, SlackMessagePosted> {

    @Override
    String parse(String message, String sessionId) {

        def matcher = (message =~ /^(?i)(<@${sessionId}>:?)\s+(?<text>(.*))/)

        if (!matcher.find()) {
            throw new RequestParseException("This message wasn't aimed at the bot.")
        }

        return matcher.group('text')
    }

    /**
     *
     * @param session
     * @param event
     * @return
     */
    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        parse(event.messageContent, session.sessionPersona().id)
        return false
    }
}
