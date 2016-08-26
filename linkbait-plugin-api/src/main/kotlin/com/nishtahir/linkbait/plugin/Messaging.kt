package com.nishtahir.linkbait.plugin

import java.io.File

/**
 *  Abstract out messaging functionality such that it's not dependent
 *  on any single framework. If a specific function is not supported by
 *  a service it's expected to throw an exception.
 */
interface Messenger {

    /**
     *  @param channel Where to send the message.
     *  @param message Content of message to send.
     *  @param unfurl Render extra content with message. Default true.
     */
    fun sendMessage(channel: String, message: String, unfurl: Boolean = true)

    /**
     *  @param user User to send the message.
     *  @param message Content of the message
     */
    fun sendDirectMessage(user: String, message: String)

    /**
     *  @param channel Where to add the reaction
     *  @param messageId ID of message to react
     *  @param reaction Reaction to react with
     */
    fun addReaction(channel: String, messageId: String, reaction: String)

    /**
     *  Removes all reaction videos on YouTube. Works 100%.
     *  They respawn pretty fast so they might be back by the time you check.
     *
     *  @param channel Where to remove the reaction
     *  @param messageId ID of message to react
     *  @param reaction Reaction to remove
     */
    fun removeReaction(channel: String, messageId: String, reaction: String)

    /**
     *
     */
    fun sendAttachment(channel: String, attachment: Attachment)

    /**
     * Upload a what to a where
     * @param channel Where
     * @param file What
     */
    fun uploadFile(channel: String, file: File)

    /**
     * @param channel where
     * @param topic new topic
     */
    fun setChannelTopic(channel: String, topic: String)

    /**
     * @return Builder to help format messages correctly for
     * the given platform.
     */
    fun getMessageBuilder(): MessageBuilder
}

/**
 * Helps format messages
 */
interface MessageBuilder {

    fun text(text: String): MessageBuilder

    fun bold(text: String): MessageBuilder

    fun italics(text: String): MessageBuilder

    fun strike(text: String): MessageBuilder

    fun pre(text: String): MessageBuilder

    fun code(text: String): MessageBuilder

    fun emoji(text: String): MessageBuilder

    fun link(title: String, url: String): MessageBuilder

    fun build(): String

}