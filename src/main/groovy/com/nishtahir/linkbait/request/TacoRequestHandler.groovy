package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.App
import com.nishtahir.linkbait.exception.RequestParseException
import com.nishtahir.linkbait.messages.Messages
import com.ullink.slack.simpleslackapi.SlackChannel
import com.ullink.slack.simpleslackapi.SlackMessageHandle
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.ReactionRemoved
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply
import groovy.json.JsonSlurper

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

@Singleton
class TacoRequestHandler extends AbstractMessageRequestHandler {

    /**
     * Timestamp of message posted by bot.
     * Required to know which votes to monitor
     */
    String timestamp

    /**
     * User ID that asked for a taco.
     */
    String user

    int upvotes

    int downvotes

    /**
     * Well...
     */
    boolean isRequestInProgress

    /**
     * Timer to expire taco request
     */
    Timer timer


    @Override
    Tuple parse(String message, String sessionId) {
        String parsedMessage =  super.parse(message,sessionId)[0];

        def matcher = parsedMessage =~ /(gimme|give|want)\s+(?<recipient>(me|<@\w+>))?(\s+(a|some))?\s+(taco|:taco:)(\s+(pls|pl(z)+))?(!+)?/

        if (!matcher.matches()) {
            throw new RequestParseException("Not a valid taco request")
        }
        return new Tuple(matcher.group('recipient'))
    }

    @Override
    void handle(SlackSession session, SlackMessagePosted event) {
        try {
            String recipient = parse(event.messageContent, session.sessionPersona().id)[0]
            if (recipient == 'me' || recipient == null) {
                user = "<@${event.sender.id}>"
            } else {
                user = recipient
            }

            int chanceOfTaco = ThreadLocalRandom.current().nextInt(10)

            if (chanceOfTaco < 2) {
                session.sendMessageOverWebSocket(event.getChannel(), "$user: congrats! You get a :taco:", null)
            } else {
                if (isRequestInProgress) {
                    session.sendMessage(event.getChannel(), "Hang on. ${user} is already asking for a taco.", null)

                } else {

                    SlackMessageHandle<SlackMessageReply> handle = session.sendMessage(event.getChannel(),
                            Messages.getRandomTacoMessage("$user"), null)
                    handle.waitForReply(1000, TimeUnit.MILLISECONDS)
                    //For some reason, they type returned GenericSlackReplyImpl throws a missing method exception
                    //The only way i could get it to work is to manually slurp the json
                    def result = new JsonSlurper().parseText(((GenericSlackReply) handle.getReply()).getPlainAnswer().toString())
                    timestamp = result['ts'].toString()
                    session.addReactionToMessage(event.getChannel(), timestamp, App.configuration.getUpvoteEmoji())
                    session.addReactionToMessage(event.getChannel(), timestamp, App.configuration.getDownvoteEmoji())

                    isRequestInProgress = true
                    upvotes = 0
                    downvotes = 0
                    timer = new Timer()
                    timer.runAfter(60000) {
                        if (isRequestInProgress) {
                            session.sendMessageOverWebSocket(event.getChannel(), Messages.getRandomTacoDeniedMessage(user), null)
                        }
                        isRequestInProgress = false
                    }
                }
            }
        } catch (RequestParseException ignore) {

        }
    }
    /**
     *
     * @param session
     * @param event
     */
    void handleVote(SlackSession session, ReactionAdded event) {
        if (isRequestInProgress && timestamp == event.messageID) {
            incrementVotes(session, event.emojiName, event.channel)
        }
    }

    /**
     *
     * @param session
     * @param event
     */
    void handleVote(SlackSession session, ReactionRemoved event) {
        if (isRequestInProgress && timestamp == event.messageID) {
            decrementVotes(session, event.emojiName, event.channel)
        }
    }

    /**
     *
     * @param session
     * @param emoji
     * @param channel
     */
    private void incrementVotes(SlackSession session, String emoji, SlackChannel channel) {
        switch (emoji) {
            case App.configuration.getUpvoteEmoji():
                upvotes++
                break;
            case App.configuration.getDownvoteEmoji():
                downvotes++
                break;
            default:
                return;
        }
        checkVotes(session, channel)
    }

    /**
     *
     * @param session
     * @param emoji
     * @param channel
     */
    private void decrementVotes(SlackSession session, String emoji, SlackChannel channel) {
        switch (emoji) {
            case App.configuration.getUpvoteEmoji():
                upvotes--
                break;
            case App.configuration.getDownvoteEmoji():
                downvotes--
                break;
            default:
                return;
        }
        checkVotes(session, channel)
    }

    /**
     * Award a taco if the vote difference > 3
     * @param session
     * @param channel
     */
    private void checkVotes(SlackSession session, SlackChannel channel){
        if (upvotes - downvotes >= 3) {
            session.sendMessageOverWebSocket(channel, "$user: congrats! You get a :taco:", null)
            isRequestInProgress = false
        }
    }
}
