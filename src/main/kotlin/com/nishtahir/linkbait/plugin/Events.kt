package com.nishtahir.linkbait.plugin

import com.google.common.eventbus.Subscribe
import ro.fortsoft.pf4j.ExtensionPoint

/**
 * Base of any event. Contains a collection of
 * generic fields that any event should have.
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
 * Nice bundle of sticks put together when a
 * message is received.
 */
class MessageEvent : Event() {

    /**
     * Message content
     */
    var message: String = ""
}

/**
 * Receives notifications when messages are received.
 */
interface MessageEventListener : ExtensionPoint {

    /**
     * Invoked when messages are received.
     * @param event message event received.
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
 * Receives notifications when reactions happen.
 */
interface ReactionEventListener : ExtensionPoint {

    /**
     *
     * @param event
     */
    @Subscribe
    fun handleReactionEvent(event: ReactionEvent);
}