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
     *
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
     *
     * @param attachment
     * @return
     */
    static SlackAttachment convertAttachmentToSlackAttachment(@NotNull Attachment attachment) {
        SlackAttachment result = new SlackAttachment()
        result.title = attachment.title
        result.titleLink = attachment.titleUrl
        result.imageUrl = attachment.imageUrl
        result.text = attachment.body
        result.color = attachment.color
        result.thumbUrl = attachment.thumbnailUrl

        attachment.additionalFields?.each { key, value ->
            result.addField(key, value, true)
        }
        return result
    }
}