package com.nishtahir.islackbot

/**
 *
 */
class Validator {


    def static final URL_PATTERN = /(?i)(https?:\\/\\/)?([\w\.-]+)\.([\w.]{2,6})([\\/\w\.-]*)*\\/?(([\w\.#\?=])*\\/?)*/

    /**
     * @param context url to match
     * @return true if valid
     */
    static boolean isValidUrl(String context) {
        context?.matches(URL_PATTERN)
    }

    /**
     * @<botid>: give me a taco.
     *
     * @param context
     * @param sessionId
     * @return true if valid
     */
    static boolean isValidTacoRequest(String context, String sessionId){
        context.matches(/^(?i)(<@${sessionId}>:)\s(gimme|give\s+me|me\s+want)(\s+(a|some))?\s+(taco|:taco:)(\s+(pls|pl(z)+))?(!+)?/)
    }
}
