package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
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
                message = "It duct tapes %s to %s".format(vendService.findRandomVend().item, vendService.findRandomVend().item)
            } else if (action[0] == "send" && action.size() > 1) {
                message = "It sends %s to %s".format(vendService.findRandomVend().item, action[1])
            } else {
                message = "It %ss %s".format(action[0].toString(), vendService.findRandomVend().item)
            }

            session.sendMessage(event.getChannel(), message, null)

            return true
        } catch (RequestParseException ignored) {
            return false
        }
    }
}
