package com.nishtahir.linkbait.core.slack

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageBuilder
import com.nishtahir.linkbait.plugin.Messenger
import com.sun.tools.corba.se.idl.constExpr.Not
import com.ullink.slack.simpleslackapi.SlackAttachment
import com.ullink.slack.simpleslackapi.SlackChannel
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.SlackUser
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

    @Override
    void sendMessage(@NotNull String channel, @NotNull String message, boolean unfurl) {
        session?.sendMessage(findChannel(channel), message)
    }

    @Override
    void sendDirectMessage(@NotNull String user, @NotNull String message) {
        session?.sendMessageToUser(findUser(user), message, null)
    }

    @Override
    void addReaction(@NotNull String channel, @NotNull String messageId, @NotNull String emoji) {
        session?.addReactionToMessage(findChannel(channel), messageId, emoji)
    }


    @Override
    void removeReaction(@NotNull String channel, @NotNull String messageId, @NotNull String emoji) {
        session?.addReactionToMessage(findChannel(channel), messageId, emoji)
    }

    @Override
    void sendAttachment(@NotNull String channel, @NotNull Attachment attachment) {
        session?.sendMessage(findChannel(channel), null, convertAttachmentToSlackAttachment(attachment))
    }

    @Override
    void uploadFile(@NotNull String channel, @NotNull File file) {
        SlackChannel slackChannel = findChannel(channel)
        if (file.exists()) {
            session.sendFile(slackChannel, file.getBytes(), file.name)
        }
    }

    @Override
    void setChannelTopic(@NotNull String channel, @NotNull String topic) {
        session?.setChannelTopic(findChannel(channel), topic)
    }

    @Override
    MessageBuilder getMessageBuilder() {
        return new SlackMessageBuilder()
    }

    /**
     *
     * @param name
     * @return
     */
    SlackChannel findChannel(@NotNull String name) {
        SlackChannel slackChannel = session?.findChannelByName(name)
        assert slackChannel != null, "Unable to find channel with name, $name"
        return slackChannel
    }

    /**
     *
     * @param name
     * @return
     */
    SlackUser findUser(@NotNull String name) {
        SlackUser slackUser = session?.findUserByUserName(name)
        assert slackUser != null, "Unable to find user with name, $name"
        return slackUser
    }

    /**
     * Turns the attachment into something that Slack can use
     * @param attachment
     * @return
     */
    static SlackAttachment convertAttachmentToSlackAttachment(@NotNull Attachment attachment) {
        return new SlackAttachment().with {
            title = attachment.title
            titleLink = attachment.titleUrl
            imageUrl = attachment.imageUrl
            text = attachment.body
            color = attachment.color
            thumbUrl = attachment.thumbnailUrl

            attachment.additionalFields?.each { key, value ->
                addField(key, value, true)
            }
            return it
        }
    }

}