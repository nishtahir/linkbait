package com.nishtahir.linkbait.core.event

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Event a reaction occurs
 */
@Canonical
@ToString
class ReactionEvent {

    /**
     * Message id
     */
    String id

    /**
     *  Channel the reaction originated
     */
    String channel

    /**
     * person that reacted
     */
    String sender

    /**
     * Reaction
     */
    String reaction

    /**
     * Reaction added
     */
    boolean added
}
