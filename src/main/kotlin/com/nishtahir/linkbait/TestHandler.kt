package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.event.*


class TestHandler : MessageEventListener, ReactionEventListener {

    val messenger : Messenger

    constructor(messenger: Messenger){
        this.messenger = messenger
    }

    override fun handleReactionEvent(reactionEvent: ReactionEvent) {
        messenger.sendMessage(reactionEvent.channel, "You reacted with :${reactionEvent.reaction}:")
    }

    override fun handleMessageEvent(messageEvent: MessageEvent) {
        messenger.sendMessage(messageEvent.channel, "echo: ${messageEvent.message}")
    }

}