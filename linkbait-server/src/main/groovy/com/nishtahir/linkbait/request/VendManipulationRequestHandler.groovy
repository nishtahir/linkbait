package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.model.Vend
import com.nishtahir.linkbait.service.UserService
import com.nishtahir.linkbait.service.VendService
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

@Singleton
class VendManipulationRequestHandler extends AbstractMessageRequestHandler {
    UserService userService
    VendService vendService

    private String ACTION_ADD = "add"
    private String ACTION_INSPECT = "inspect"
    private String ACTION_REMOVE = "remove"

    def setUserService(UserService userService) {
        this.userService = userService
    }

    def setVendService(VendService vendService) {
        this.vendService = vendService
    }

    @Override
    Tuple parse(String message, String sessionId) {
        String parsedMessage = super.parse(message, sessionId)[0]

        // https://regex101.com/r/uC1yD5/2
        def matcher = parsedMessage =~ /vend (?<action>(add|inspect|remove)) (?<args>(.*))/

        if (!matcher.matches()) {
            throw new RequestParseException("Not a valid vend manipulation request")
        }

        return new Tuple(matcher.group("action"), matcher.group("args"))
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            Tuple action = parse(event.messageContent, session.sessionPersona().id)
            String item = action[1].toString().trim()

            if (action[0] == ACTION_ADD) {
                if (addVend(item, event.sender.id, event.sender.realName)) {
                    session.sendMessage(event.channel, "Vend added!", null)
                } else {
                    session.sendMessage(event.channel, "There was an error", null)
                }
            } else if (action[0] == ACTION_INSPECT) {
                Vend vend = vendService.findVendByItem(item)

                if (vend == null) {
                    session.sendMessage(event.channel, "Unable to find that vend", null)
                } else {
                    session.sendMessage(event.channel, "Item: ${vend.item}, Rarity: ${vend.rarity}, Publisher: ${vend.publisher}", null)
                }
            } else if (action[0] == ACTION_REMOVE) {
                if (!event.sender.admin) {
                    session.sendMessage(event.channel, "No way José", null)
                    return true
                }

                Vend vend = vendService.findVendByItem(item)
                if (vend != null) {
                    vendService.deleteVend(vend)
                    session.sendMessage(event.channel, "Vend removed", null)
                } else {
                    session.sendMessage(event.channel, "Unable to find that vend", null)
                }
            }

            return true
        } catch (RequestParseException ignored) {
            return false
        }
    }

    private boolean addVend(String item, String slackUserId, String slackRealName) {
        User user = userService.findUserBySlackUserId(slackUserId)
        if (user == null) {
            user = userService.createUser(new User(slackUserId: slackUserId, username: slackRealName))
        }

        Vend vend = new Vend(item: item, rarity: Vend.Rarity.RARE, publisher: user)
        vendService.createOrPromoteVend(vend)
        return true
    }
}
