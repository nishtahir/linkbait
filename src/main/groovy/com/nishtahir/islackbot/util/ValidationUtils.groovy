package com.nishtahir.islackbot.util

import com.nishtahir.islackbot.Patterns

/**
 *  Utility class to help with data validation.
 */
class ValidationUtils {

    /**
     *
     */
    def static final SlACK_URL_PATTERN = /(?i)<(?<url>(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \-&\?=%+\.]*)*\/?)(|.*)?>/

    /**
     * @param context url to match
     * @return true if valid
     */
    static boolean isValidUrl(String context) {
        context.matches(SlACK_URL_PATTERN)
    }

    static String getUrlFromSlackLink(String context){
        def matcher = (context =~ SlACK_URL_PATTERN)
        if(matcher.find()){
            return matcher.group('url')
        }

        return null
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

    static String getPlaystoreId(String context) {
        def matcher = (context =~ /play\.google.com\\/store\\/apps\\/details\?id=(?<id>[\w\.]+)/)
        if(matcher.find()){
            return matcher.group('id')
        }
        return null
    }
}
