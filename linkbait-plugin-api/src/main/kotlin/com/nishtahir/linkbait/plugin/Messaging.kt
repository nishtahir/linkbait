package com.nishtahir.linkbait.plugin

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
    fun sendAttachment(channel: String, attachment : Attachment)
}