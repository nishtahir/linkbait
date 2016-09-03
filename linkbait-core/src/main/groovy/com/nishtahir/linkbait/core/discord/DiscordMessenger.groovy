package com.nishtahir.linkbait.core.discord

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageBuilder
import com.nishtahir.linkbait.plugin.Messenger
import groovy.transform.Canonical
import org.jetbrains.annotations.NotNull
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel

@Canonical
class DiscordMessenger implements Messenger {

    /**
     * Our main entry to the world of Discord
     */
    IDiscordClient client;

    @Override
    void sendMessage(@NotNull String channel, @NotNull String message, boolean unfurl) {
        findChannelByName(channel)?.sendMessage(message)
    }

    @Override
    void sendDirectMessage(@NotNull String user, @NotNull String message) {
        // TODO Implement this method.
    }

    @Override
    void addReaction(@NotNull String channel, @NotNull String messageId, @NotNull String reaction) {
        // Not supported
    }

    @Override
    void removeReaction(@NotNull String channel, @NotNull String messageId, @NotNull String reaction) {
        // Not supported
    }

    @Override
    void sendAttachment(@NotNull String channel, @NotNull Attachment attachment) {
        findChannelByName(channel).sendMessage(convertAttachmentToSimpleMessage(attachment))
    }

    @Override
    void uploadFile(@NotNull String channel, @NotNull File file) {
        findChannelByName(channel)?.sendFile(file)
    }

    @Override
    void setChannelTopic(@NotNull String channel, @NotNull String topic) {

    }

    private IChannel findChannelByName(String name) {
        def matchedChannels = client.getChannels(true).findAll {it.name == name}
        if (matchedChannels.size() == 1) {
            return matchedChannels.first()
        }

        return null
    }

    private static String convertAttachmentToSimpleMessage(Attachment attachment) {
        String msg = ""
        msg += "**${attachment.title}**\n"
        if (attachment.titleUrl) {
            msg += "__${attachment.titleUrl}__\n"
        }
        msg += "${attachment.body}\n"

        if (attachment.additionalFields) {
            msg += "\n"
        }

        attachment.additionalFields?.each { key, value ->
            msg += "**$key**: *$value*\n"
        }

        return removeAttachmentLinks(msg)
    }

    private static String removeAttachmentLinks(String message) {
        def matcher = (message =~ /<.*?:\\/\\/.*?\|(.+?)>/)
        while (matcher.find()) {
            matcher.each { ArrayList<String> match ->
                message = message.replace(match[0], match[1].trim())
            }
        }
        return message
    }

    @Override
    MessageBuilder getMessageBuilder() {
        return null
    }
}