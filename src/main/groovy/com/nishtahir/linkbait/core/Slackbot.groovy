package com.nishtahir.linkbait.core

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.event.MessageEvent
import com.nishtahir.linkbait.core.event.Messenger
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
class SlackBot extends AbstractBot {

    /**
     *  Where all the communication magic happens
     */
    private SlackSession session

    /**
     *  Provided by Slack for access to the team
     */
    private String apiToken

    /**
     *
     */
    private Messenger messenger

    /**
     * Bus to carry all of the sweet sweet messages across
     */

    SlackBot(String apiToken, String owner) {
        this.apiToken = apiToken;
        this.owner = owner

        eventBus = new EventBus(owner)
        init(apiToken)
    }

    /**
     * Initialize even and start session
     * @param apiToken for slack
     * @param owner identifies the eventbus and other properties
     */
    private void init(String apiToken) {
        session = SlackSessionFactory.createWebSocketSlackSession(apiToken);
        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            void onEvent(SlackMessagePosted event, SlackSession session) {
                MessageEvent messageEvent = new MessageEvent(id: event.timestamp,
                        channel: event.channel.name,
                        sender: event.sender.id,
                        message: event.messageContent)
                if (event.sender.id != session.sessionPersona().id) {
                    eventBus.post(messageEvent)
                }
            }
        })
        session.addReactionAddedListener(new ReactionAddedListener() {
            @Override
            void onEvent(ReactionAdded event, SlackSession session) {
                ReactionEvent reactionEvent = new ReactionEvent(id: event.messageID,
                        channel: event.channel.name,
                        sender: event.user.id,
                        message: event.emojiName,
                        added: true)
                if (event.getUser().id != session.sessionPersona().id) {
                    eventBus.post(reactionEvent)
                }
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
                if (event.getUser().id != session.sessionPersona().id) {
                    eventBus.post(reactionEvent)
                }
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

    @Override
    Messenger getMessenger() {
        if (messenger == null) {
            messenger = new SlackMessenger(session: session)
        }
        return messenger
    }

}
