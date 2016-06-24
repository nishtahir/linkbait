package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.event.MessageEvent
import com.nishtahir.linkbait.core.event.ReactionEvent
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.ReactionRemoved
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 *
 */
@Canonical
@ToString
class Slackbot extends AbstractBot {
    /**
     *
     */
    SlackSession session

    /**
     *
     */
    String apiToken

    /**
     *
     */
    EventBus eventBus


    Slackbot(String apiToken, String owner) {
        this.apiToken = apiToken;
        init(apiToken, owner)
    }

    /**
     * Initialize even and start session
     * @param apiToken for slack
     * @param owner identifies the eventbus and other properties
     */
    private void init(String apiToken, String owner) {
        eventBus = new EventBus(owner)
        session = SlackSessionFactory.createWebSocketSlackSession(apiToken);
        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            void onEvent(SlackMessagePosted event, SlackSession session) {
                MessageEvent messageEvent = new MessageEvent(id: event.timestamp,
                        channel: event.channel.id,
                        sender: event.sender.id,
                        message: event.messageContent)
                eventBus.post(messageEvent)
            }
        })
        session.addReactionAddedListener(new ReactionAddedListener() {
            @Override
            void onEvent(ReactionAdded event, SlackSession session) {
                ReactionEvent reactionEvent = new ReactionEvent(id: event.messageID,
                        channel: event.channel.id,
                        sender: event.user.id,
                        message: event.emojiName,
                        added: true)
                eventBus.post(reactionEvent)
            }
        })
        session.addReactionRemovedListener(new ReactionRemovedListener() {
            @Override
            void onEvent(ReactionRemoved event, SlackSession session) {
                ReactionEvent reactionEvent = new ReactionEvent(id: event.messageID,
                        channel: event.channel.id,
                        sender: event.user.id,
                        reaction: event.emojiName,
                        added: true)
                eventBus.post(reactionEvent)
            }
        })
    }

    /**
     * Starts a slack session
     */
    public void start() {
        session?.connect()
    }

    /**
     * Stops the slack session
     */
    public void stop() {
        session?.disconnect()
    }

}
