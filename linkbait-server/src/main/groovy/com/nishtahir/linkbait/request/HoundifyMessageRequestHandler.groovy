package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.nishtahir.linkbait.houndify.HoundifyClient
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import groovy.json.JsonSlurper
/**
 * Created by maxke on 24.03.2016.
 * Handles an arbitrary message
 */
@Singleton
class HoundifyMessageRequestHandler extends AbstractMessageRequestHandler {

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            String text = parse(event.messageContent, session.sessionPersona().id)[0]

            String response = HoundifyClient.doTextRequest(text)
            def root = new JsonSlurper().parseText(response)

            if (root.NumToReturn > 0 && root.Status.equals("OK")) {
                String reply = root.AllResults.get(0).WrittenResponseLong
                session.sendMessage(event.channel, reply, null)
            } else {
                session.sendMessage(event.channel, "I'm sorry, but I don't have an answer to that. Possibly I ran out of API credits.", null)
            }
            return true
        } catch (RequestParseException ignored) {
            return false
        }
    }
}
