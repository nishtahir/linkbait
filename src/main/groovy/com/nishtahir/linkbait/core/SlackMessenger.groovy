package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.event.Messenger
import com.ullink.slack.simpleslackapi.SlackChannel
import com.ullink.slack.simpleslackapi.SlackSession
import groovy.transform.Canonical

/**
 * Messenger implementation for slack.
 * This allows other parts of the app access to certain actions
 */
@Canonical
class SlackMessenger implements Messenger {

    SlackSession session

    @Override
    void sendMessage(String channel, String message) {
        SlackChannel slackChannel  = session.findChannelByName(channel)
        session?.sendMessage(slackChannel, message)
    }
}