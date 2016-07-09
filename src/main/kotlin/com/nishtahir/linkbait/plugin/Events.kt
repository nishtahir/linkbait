package com.nishtahir.linkbait.plugin

import com.google.common.eventbus.Subscribe

/**
 *
 */
abstract class Event {

    /**
     * Id of event
     */
    var id: String = ""

    /**
     * person that reacted
     */
    var sender: String = ""

    /**
     * Channel the message originated
     */
    var channel: String = ""

}

/**
 * Event called when a message is sent
 */
class MessageEvent : Event() {

    /**
     * Message content
     */
    var message: String = ""
}

/**
 *
 */
interface MessageEventListener {

    /**
     *
     * @param event
     */
    @Subscribe
    fun handleMessageEvent(event: MessageEvent)
}

/**
 *
 */
class ReactionEvent : Event() {

    /**
     * Reaction
     */
    var reaction: String = ""
    /**
     * Reaction added
     */
    var added: Boolean = false
}

/**
 *
 */
interface ReactionEventListener {

    /**
     *
     * @param event
     */
    @Subscribe
    fun handleReactionEvent(event: ReactionEvent);
}