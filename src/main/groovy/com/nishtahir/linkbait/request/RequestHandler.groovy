package com.nishtahir.linkbait.request

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackEvent

/**
 * Base class for making requests to the bot.
 * @param <T> Information passed through from {@link RequestHandler#parse} to {@link RequestHandler#parse}.
 * @param <E> Subclass of {@link SlackEvent}.
 */
interface RequestHandler<T, E extends SlackEvent> {

    /**
     * Parse the request and throw a {@link com.nishtahir.linkbait.exception.RequestParseException} if invalid.
     * @return Object that is passed to {@link #handle}.
     */
     T parse(SlackSession session, E event)
    /**
     * In case of no {@link com.nishtahir.linkbait.exception.RequestParseException}, execute this.
     * @param parsed
     * @see com.nishtahir.linkbait.exception.RequestParseException
     */
    void handle(SlackSession session, E event)
}