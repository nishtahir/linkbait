package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.nishtahir.linkbait.messages.Messages
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

@Singleton
class HelpRequestHandler extends AbstractMessageRequestHandler {

    @Override
    Tuple parse(SlackSession session, SlackMessagePosted event) {
        String message = super.parse(session, event)[0];

        def matcher = message =~ /help(\s?me.*)?\S*?$/

        if (!matcher.matches()) {
            throw new RequestParseException("Not a help request.")
        }
        return null
    }

    @Override
    void handle(SlackSession session, SlackMessagePosted event) {
        try {
            parse(session, event)
            session.sendMessageToUser(event.sender,
                    Messages.getHelpMessage(session.sessionPersona().userName),
                    null
            )
        } catch (RequestParseException ignore) {

        }
    }
}
