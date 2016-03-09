package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.messages.Messages
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

class HelpRequest extends MessageRequest {

    HelpRequest(SlackMessagePosted event, SlackSession session) {
        super(event, session)
    }

    @Override
    protected Tuple parse() {
        Tuple parsed = super.parse()
        if(parsed == null)
            return null

        String message = parsed[0];
        def matcher = message =~ /help(\s?me.*)?\S*?$/
        if(!matcher.matches())
            throw new ParseException("Not a help request.")

        return parsed
    }

    @Override
    protected void act(Tuple p) {
        this.session.sendMessageToUser(
            event.sender,
            Messages.getHelpMessage(this.session.sessionPersona().userName),
            null
        )
    }
}
