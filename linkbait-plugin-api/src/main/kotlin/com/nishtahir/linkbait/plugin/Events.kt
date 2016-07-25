package com.nishtahir.linkbait.plugin

import com.google.common.eventbus.Subscribe
import com.nishtahir.linkbait.plugin.model.Event
import com.nishtahir.linkbait.plugin.model.EventListener


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
interface MessageEventListener : EventListener{

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
interface ReactionEventListener : EventListener{

    /**
     *
     * @param event
     */
    @Subscribe
    fun handleReactionEvent(event: ReactionEvent);
}