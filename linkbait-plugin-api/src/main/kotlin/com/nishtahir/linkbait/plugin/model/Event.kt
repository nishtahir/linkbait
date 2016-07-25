package com.nishtahir.linkbait.plugin.model

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