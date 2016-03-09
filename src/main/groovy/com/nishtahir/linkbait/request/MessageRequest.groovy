package com.nishtahir.linkbait.request

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

/**
 * {@link Request} subclass specifically targeted to the bot in a message.
 */
abstract class MessageRequest extends Request<Tuple, SlackMessagePosted> {

    MessageRequest(SlackMessagePosted event, SlackSession session) {
        super(event, session)
    }

    @Override
    protected Tuple parse() {
        String message = this.event.messageContent

        def matcher = (message =~ /^(?i)(<@${this.session.sessionPersona().id}>:?)\s+(?<text>(.*))/)

        if(!matcher.find())
            throw new ParseException("This message wasn't aimed at the bot.")

        String text = matcher.group('text')
        return new Tuple(text)
    }
}
