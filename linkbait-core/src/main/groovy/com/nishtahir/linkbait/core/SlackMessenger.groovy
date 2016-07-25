package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.plugin.Messenger
import com.ullink.slack.simpleslackapi.SlackChannel
import com.ullink.slack.simpleslackapi.SlackSession
import groovy.transform.Canonical

/**
 * Messenger implementation for slack.
 * This allows other parts of the app access to certain actions
 */
@Canonical
class SlackMessenger implements Messenger {

    /**
     *
     */
    SlackSession session

    /**
     *
     * @param channel
     * @param message
     */
    @Override
    void sendMessage(String channel, String message) {
        SlackChannel slackChannel = session.findChannelByName(channel)
        session?.sendMessage(slackChannel, message)
    }

    /**
     *
     * @param channel
     * @param messageId
     * @param emoji
     */
    void addReaction(String channel, String messageId, String emoji) {
        SlackChannel slackChannel = session.findChannelByName(channel)
        session?.addReactionToMessage(slackChannel, messageId, emoji)
    }

    @Override
    void removeReaction(String channel, String messageId, String emoji) {
        SlackChannel slackChannel = session.findChannelByName(channel)
        session?.addReactionToMessage(slackChannel, messageId, emoji)
    }
}