package com.nishtahir.islackbot.util

import com.nishtahir.islackbot.Patterns

/**
 *  Utility class to help with data validation.
 */
class ValidationUtils {

    /**
     *  @deprecated
     *  Could be improved. Currently matches silly things like
     *  spaghetti...
     */
    def static final URL_PATTERN = /(?i)(https?:\\/\\/)?([\w\.-]+)\.([\w.]{2,6})([\\/\w\.-]*)*\\/?(([\w\.#\?=])*\\/?)*/

    /**
     * @param context url to match
     * @return true if valid
     */
    static boolean isValidUrl(String context) {
        Patterns.WEB_URL.matcher(context).matches()
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
