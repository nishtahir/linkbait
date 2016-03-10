package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * {@link RequestHandler} subclass specifically targeted to the bot in a message.
 */
abstract class AbstractMessageRequestHandler implements RequestHandler<Tuple, SlackMessagePosted> {

    @Override
    Tuple parse(SlackSession session, SlackMessagePosted event) {
        String message = event.messageContent

        def matcher = (message =~ /^(?i)(<@${session.sessionPersona().id}>:?)\s+(?<text>(.*))/)

        if(!matcher.find())
            throw new RequestParseException("This message wasn't aimed at the bot.")

        String text = matcher.group('text')
        return new Tuple(text)
    }
}
