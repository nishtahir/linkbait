package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * {@link RequestHandler} subclass specifically targeted to the bot in a message.
 */
abstract class AbstractMessageRequestHandler implements RequestHandler<Tuple, SlackMessagePosted> {

    @Override
    Tuple parse(String message, String sessionId) {

        def matcher = (message =~ /^(?i)(<@${sessionId}>:?)\s+(?<text>(.*))/)

        if(!matcher.find())
            throw new RequestParseException("This message wasn't aimed at the bot.")

        String text = matcher.group('text')
        return new Tuple(text)
    }
}
