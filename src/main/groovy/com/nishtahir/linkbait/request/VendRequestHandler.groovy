package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

@Singleton
class VendRequestHandler extends AbstractMessageRequestHandler {

    @Override
    Tuple parse(String message, String sessionId) {
        String parsedMessage = super.parse(message, sessionId)[0]

        /**
         * Explanation and testing here: https://regex101.com/r/hZ2lR8/2
         */
        def generalMatcher = parsedMessage =~ /(?<action>(\b[a-zA-Z]{0,10}end\b))(\s+)?(?<recipient>.{1,30})?/

        if (!generalMatcher.matches()) {
            throw new RequestParseException("Not a valid vend request")
        }

        return new Tuple(generalMatcher.group("action"), generalMatcher.group("recipient"))
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            Tuple action = parse(event.messageContent, session.sessionPersona().id)

            // TODO: Get objects from a vendlist somewhere
            session.sendMessage(event.getChannel(), String.format("It %ss a vended object", action[0]), null)

            return true
        } catch (RequestParseException ignored) {
            return false
        }
    }
}
