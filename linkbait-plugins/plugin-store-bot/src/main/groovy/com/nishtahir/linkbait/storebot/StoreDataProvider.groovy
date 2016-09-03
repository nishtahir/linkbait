package com.nishtahir.linkbait.storebot

import com.nishtahir.linkbait.plugin.Attachment
import org.apache.commons.lang3.StringEscapeUtils

import java.util.regex.Pattern

/**
 * Created by nish on 7/27/16.
 */
abstract class StoreDataProvider {

    static final int MAX_DESC_LENGTH = 2000;

    String url

    StoreDataProvider(String url) {
        this.url = url
    }

    abstract Attachment getAttachment()

    static String makeFancyDescription(String input) {
        String result = input.replaceAll(Pattern.compile('</?(strong|b)>'), '*') // Slack supports bold formatting
        result = result.replaceAll(Pattern.compile('</?i>'), '_') // Also italics
        result = result.replaceAll(Pattern.compile('<li>'), '• ') // We can have nice bullet points
        result = StringEscapeUtils.unescapeHtml4(result) // Replaces HTML entities by their character representations

        result = result.replaceAll(Pattern.compile('<[^>]*>'), '') // And now get rid off all the remaining crap

        return result
    }

    /**
     *
     * @param context
     * @return
     */
    static String getPlayStoreId(String context) {
        def matcher = (context =~ /play\.google.com\\/store\\/apps\\/details\?id=(?<id>[\w.]+)/)
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