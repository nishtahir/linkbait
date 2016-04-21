package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.service.VendService
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

@Singleton
class VendRequestHandler extends AbstractMessageRequestHandler {
    VendService vendService

     def setVendService(VendService vendService) {
         this.vendService = vendService
     }

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


            String message
            if (vendService == null) {
                message = "It vends is currently disabled"
            } else if (action[0] == "append") {
                String item1 = vendService.getRandomVend().item
                String item2 = vendService.getRandomVend().item
                message = "It duct tapes $item1 to $item2"
            } else if (action[0] == "send") {
                String item = vendService.getRandomVend().item
                String recipient

                if (action[1] != null) {
                    recipient = action[1]

                } else {
                    recipient = "<@${event.sender.id}>"
                }

                message = "It sends $item to $recipient"
            } else {
                String act = action[0] + "s"
                String item = vendService.getRandomVend().item
                message = "It $act $item"
            }

            session.sendMessage(event.getChannel(), message, null)

            return true
        } catch (RequestParseException ignored) {
            return false
        }
    }
}
