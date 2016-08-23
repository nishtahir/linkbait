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
     */
    fun sendMessage(channel: String, message: String)

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
     *
     */
    fun getMessageFormatter(): MessageFormatter
}

interface MessageFormatter {

    fun format(): String

    fun par(text: String)

    fun bold(text: String)

    fun italics(text: String)

    fun strike(text: String)

    fun pre(text: String)

    fun code(text: String)

    fun emoji(text: String)

    fun link(title: String, url: String)

    fun clear()
}

class AbstractMessageFormatter : MessageFormatter {

    override fun format(): String {
        throw UnsupportedOperationException()
    }

    override fun par(text: String) {
        throw UnsupportedOperationException()
    }

    override fun bold(text: String) {
        throw UnsupportedOperationException()
    }

    override fun italics(text: String) {
        throw UnsupportedOperationException()
    }

    override fun strike(text: String) {
        throw UnsupportedOperationException()
    }

    override fun pre(text: String) {
        throw UnsupportedOperationException()
    }

    override fun code(text: String) {
        throw UnsupportedOperationException()
    }

    override fun emoji(text: String) {
        throw UnsupportedOperationException()
    }

    override fun link(title: String, url: String) {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        throw UnsupportedOperationException()
    }

}