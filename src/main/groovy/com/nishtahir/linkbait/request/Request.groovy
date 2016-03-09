package com.nishtahir.linkbait.request

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackEvent

/**
 * Base class for making requests to the bot.
 * @param <T> Information passed through from {@link Request#parse} to {@link Request#parse}.
 * @param <E> Subclass of {@link SlackEvent}.
 */
abstract class Request<T, E extends SlackEvent> {

    /**
     * SlackEvent linked to the {@link Request}.
     */
    protected final E event;
    /**
     * SlackSession linked to the {@link Request}.
     */
    protected final SlackSession session;

    Request(E event, SlackSession session) {
        this.session = session;
        this.event = event;
        try {
            T parsed = (T) this.parse()
            this.act(parsed)
        } catch(ParseException ignored) {

        }
    }

    /**
     * Parse the request and throw a {@link ParseException} if invalid.
     * @return Object that is passed to {@link #act(T)}.
     * @see #act(T)
     */
    protected abstract T parse();
    /**
     * In case of no {@link ParseException}, execute this.
     * @param parsed
     * @see ParseException
     */
    protected abstract void act(T parsed);
}