package com.nishtahir.linkbait.util
/**
 *  Utility class to help with data validation.
 */
class ValidationUtils {

    static final String URL_PATTERN = /(?<url>(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \-&\?=%+\.]*)*\/?)/

    /**
     *
     */
    def static final SlACK_URL_PATTERN = /(?i)<$URL_PATTERN(|.*)?>/

    static boolean isValidUrl(String context) {
        context.matches(URL_PATTERN)
    }

    /**
     * @param context url to match
     * @return true if valid
     */
    static boolean isValidSlackUrlFormat(String context) {
        context.matches(SlACK_URL_PATTERN)
    }

    static String getUrlFromSlackLink(String context) {
        def matcher = (context =~ SlACK_URL_PATTERN)
        if (matcher.find()) {
            return matcher.group('url')
        }

        return null
    }

    /**
     * @param context
     * @return app identifier for play store app
     */
    static String getPlaystoreId(String context) {
        def matcher = (context =~ /play\.google.com\\/store\\/apps\\/details\?id=(?<id>[\w\.]+)/)
        if (matcher.find()) {
            return matcher.group('id')
        }
        return null
    }

    /**
     * @param context
     * @return unique steam id. -1 if invalid
     */
    static long getSteamId(String context) {
        def matcher = (context =~ /https?:\\/\\/store.steampowered.com(\\/agecheck)?\\/app\\/(?<id>\d+)/)
        if (matcher.find()) {
            return Long.valueOf(matcher.group('id'))
        }
        return -1
    }
}
