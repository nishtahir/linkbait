package com.nishtahir.islackbot

/**
 *
 */
class Validator {


    def static final URL_PATTERN = /(https?:\\/\\/)?([\w\.-]+)\.([\w.]{2,6})([\\/\w\.-]*)*\\/?(([\w\.#\?=])*\\/?)*/

    /**
     * @param context url to match
     * @return true if valid
     */
    static boolean isValidUrl(String context) {
        context?.matches(URL_PATTERN)
    }
}
