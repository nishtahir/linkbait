package com.nishtahir.linkbait.util

import com.ullink.slack.simpleslackapi.SlackAttachment
import org.apache.commons.lang3.StringEscapeUtils

import java.util.regex.Pattern

/**
 * Base class for classes that pull some info from an URL and create a Slack attachment
 */
abstract class AttachmentCreator {
    /**
     * Although slack truncates descriptions,
     * it's probably not a good idea to send to many
     * characters in the attachment.
     */
    static final int MAX_DESC_LENGTH = 2000;

    /**
     * Generates an attachment from the given URL. Subclasses request data and put it into the attachment
     * @param url URL to request data from
     * @return A {@link SlackAttachment} object which can be attached to a message
     */
    abstract SlackAttachment getSlackAttachmentForUrl(String url)

    /**
     * Sanitize a String that contains formastting with HTML
     * @param input HTML ridden String
     * @return Beautifully formatted String. Make sure to set <pre>attachment.markdown_in = ['text']</pre>!
     */
    static String makeFancyDescription(String input) {
        String result = input.replaceAll(Pattern.compile('</?(strong|b)>'), '*') // Slack supports bold formatting
        result = result.replaceAll(Pattern.compile('</?i>'), '_') // Also italics
        result = result.replaceAll(Pattern.compile('<li>'), '• ') // We can have nice bullet points
        result = StringEscapeUtils.unescapeHtml4(result) // Replaces HTML entities by their character representations

        result = result.replaceAll(Pattern.compile('<[^>]*>'), '') // And now get rid off all the remaining crap

        return result
    }
}
