package com.nishtahir.linkbait.util

/**
 * Delicious class for giving out delicious treats.
 */
class TacoUtils {

    /**
     * @param context String to match
     * @param sessionId id of the bot for current session
     * @return true if valid
     */
    static boolean isValidTacoRequest(String context, String sessionId) {
        return context.matches(/^(?i)(<@${sessionId}>:?)\s+(gimme|give|want)\s+(?<recipient>(me|<@\w+>))?(\s+(a|some))?\s+(taco|:taco:)(\s+(pls|pl(z)+))?(!+)?/)
    }

    /**
     * @<botid>: give me a taco.
     * @<botid>: give @user a taco
     *
     * @param context
     * @param sessionId
     * @return true if valid
     */
    static String parseTacoRequest(String context, String sessionId) {
        def matcher = (context =~ /^(?i)(<@${sessionId}>:?)\s+(gimme|give|want)\s+(?<recipient>(me|<@\w+>))?(\s+(a|some))?\s+(taco|:taco:)(\s+(pls|pl(z)+))?(!+)?/)
        return matcher.find() ? matcher.group('recipient') : null
    }
}
