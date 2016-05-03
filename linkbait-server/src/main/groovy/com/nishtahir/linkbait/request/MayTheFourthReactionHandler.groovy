package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.RequestHandler
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

import java.util.concurrent.ThreadLocalRandom

/**
 * I'll leave it to you to guess what this is for
 */
@Singleton
class MayTheFourthReactionHandler implements RequestHandler<Void, SlackMessagePosted> {

    static String[] emojis = [
            "deathstar",
            "stormtrooper",
            "yoda",
            "chewbacca",
            "xwing",
            "ywing",
            "tiefighter",
            "r2d2",
            "c3po",
            "bobafett",
            "deathstar2",
            "rwing",
            "milleniumfalcon",
            "jarjarbinks"
    ]

    @Override
    Void parse(String message, String sessionId) {
        return null
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        if ('hangout'.equals(event.channel.name) || 'linkbait-testing'.equals(event.channel.name)) {
            try {
                int likelyhoodOfReaction = ThreadLocalRandom.current().nextInt(10)
                if (likelyhoodOfReaction > 7) {
                    for (i in 1..ThreadLocalRandom.current().nextInt(1, 3)) {
                        Timer timer = new Timer()
                        timer.runAfter(i * 1000) {
                            session.addReactionToMessage(event.channel, event.timestamp, emojis[ThreadLocalRandom.current().nextInt(emojis.length)])
                        }
                    }
                }
            } catch (RequestParseException ignore) {

            }
        }
        //Never interrupt regular tasks
        return false
    }
}