package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.Messenger
import com.ullink.slack.simpleslackapi.SlackAttachment
import com.ullink.slack.simpleslackapi.SlackChannel
import com.ullink.slack.simpleslackapi.SlackSession
import groovy.transform.Canonical
import org.jetbrains.annotations.NotNull

/**
 * Messenger implementation for slack.
 * This allows other parts of the app access to certain actions
 */
@Canonical
class SlackMessenger implements Messenger {

    /**
     * Session context. Needed to send the right messages to the right slack.
     */
    SlackSession session

    /**
     *
     * @param channel
     * @param message
     */
    @Override
    void sendMessage(@NotNull String channel, @NotNull String message) {
        SlackChannel slackChannel = session?.findChannelByName(channel)
        session?.sendMessage(slackChannel, message)
    }

    /**
     *
     * @param channel
     * @param messageId
     * @param emoji
     */
    void addReaction(@NotNull String channel, @NotNull String messageId, @NotNull String emoji) {
        SlackChannel slackChannel = session?.findChannelByName(channel)
        session?.addReactionToMessage(slackChannel, messageId, emoji)
    }

    @Override
    void removeReaction(@NotNull String channel, @NotNull String messageId, @NotNull String emoji) {
        SlackChannel slackChannel = session?.findChannelByName(channel)
        session?.addReactionToMessage(slackChannel, messageId, emoji)
    }

    @Override
    void sendAttachment(@NotNull String channel, @NotNull Attachment attachment) {
        SlackChannel slackChannel = session?.findChannelByName(channel)
        session.sendMessage(slackChannel, null, convertAttachmentToSlackAttachment(attachment))
    }

    /**
     * Turns the attachment into something that Slack can use
     * @param attachment
     * @return
     */
    static SlackAttachment convertAttachmentToSlackAttachment(@NotNull Attachment attachment) {
        SlackAttachment slackAttachment = new SlackAttachment()
        slackAttachment.title = attachment.title
        slackAttachment.titleLink = attachment.titleUrl
        slackAttachment.imageUrl = attachment.imageUrl
        slackAttachment.text = attachment.body
        slackAttachment.color = attachment.color
        slackAttachment.thumbUrl = attachment.thumbnailUrl

        attachment.additionalFields?.each { key, value ->
            slackAttachment.addField(key, value, true)
        }
        return slackAttachment
    }
}