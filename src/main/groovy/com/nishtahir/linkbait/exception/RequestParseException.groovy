package com.nishtahir.linkbait.exception

/**
 * Thrown when a message doesn't match a given
 * request context.
 */
class RequestParseException extends Exception {

    /**
     * {@inheritDoc}
     */
    RequestParseException(String message) {
        super(message)
    }
}
