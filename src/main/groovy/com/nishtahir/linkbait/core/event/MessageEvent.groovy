package com.nishtahir.linkbait.core.event

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Event called when a message is sent
 */
@Canonical
@ToString
class MessageEvent {

    /**
     * Message id
     */
    String id

    /**
     *  Channel the message originated
     */
    String channel

    /**
     * Sender of the message
     */
    String sender

    /**
     * Message content
     */
    String message
}
