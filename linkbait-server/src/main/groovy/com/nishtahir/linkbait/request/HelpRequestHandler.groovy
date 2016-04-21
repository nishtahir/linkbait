package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.messages.Messages
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

@Singleton
class HelpRequestHandler extends AbstractMessageRequestHandler {

    @Override
    Tuple parse(String message, String sessionId) {
        String parsedMessage = super.parse(message, sessionId)[0];

        def matcher = parsedMessage =~ /help(\s?me.*)?\S*?$/

        if (!matcher.matches()) {
            throw new RequestParseException("Not a help request.")
        }
        return null
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            parse(event.messageContent, session.sessionPersona().id)
            session.sendMessageToUser(event.sender,
                    Messages.getHelpMessage(session.sessionPersona().userName),
                    null
            )
            return true
        } catch (RequestParseException ignore) {
            return false
        }
    }
}
