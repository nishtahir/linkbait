package com.nishtahir.islackbot.util

import com.ullink.slack.simpleslackapi.SlackAttachment
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Playstore-bot functionality
 */
class PlayStoreUtils {

    /**
     * Although slack truncates descriptions,
     * it's probably not a good idea to send to many
     * characters in the attachment.
     */
    static final int MAX_DESC_LENGTH = 2000;

    /**
     * Parse playstore app content
     * @param playId
     * @return
     */
    static Map<String, String> getAppDetailsFromPlayStore(String link) {
        String html = link.toURL().text
        Document doc = Jsoup.parse(html)

        def fields = [:]

        fields.put('title', doc.select('.document-title div').text().trim())
        fields.put('author', doc.select('div[itemprop=author] span[itemprop=name]').text().trim())
        fields.put('desc', escapeHtml(doc.select('div[itemprop=description]').text().take(MAX_DESC_LENGTH)))
        fields.put('imageUrl', doc.select('.cover-image').attr('src').trim())
        fields.put('score', doc.select('.score').text().trim())
        fields.put('reviews', doc.select('.reviews-num').text().trim())
        fields.put('lastUpdated', doc.select('div[itemprop=datePublished]').text().trim())
        fields.put('dlCount', doc.select('div[itemprop=numDownloads]').text().trim())
        fields.put('version', doc.select('div[itemprop=softwareVersion]').text().trim())
        fields.put('osVersion', doc.select('div[itemprop=operatingSystems]').text().trim())
        fields.put('contentRating', doc.select('div[itemprop=contentRating]').text().trim())
        fields.put('price', doc.select('meta[itemprop=price]').attr('content').trim().replace("0", "Free"))

        return fields
    }

    /**
     * All hail his Majesty, @nish!
     *
     * @param playId
     * @return details as slack attachment
     */
    static SlackAttachment getPlayStoreDetailsAsSlackAttachment(String playId){
        String url = getUrlFromPlayId(playId)
        Map<String, String> appInfo = getAppDetailsFromPlayStore(playId)

        SlackAttachment attachment = new SlackAttachment()
        attachment.fallback = appInfo['title']
        attachment.title = appInfo['title']
        attachment.titleLink = url
        attachment.thumb_url = appInfo['imageUrl']
        attachment.color = '3F51B5' //Material Indigo 500
        attachment.text = appInfo['desc']
        attachment.addField("Author", appInfo['author'], true)
        attachment.addField("Price", appInfo['price'], true)
        attachment.addField("Last updated", appInfo['lastUpdated'], true)
        attachment.addField("Downloads", appInfo['dlCount'], true)
        attachment.addField("All hail his Majesty", "@nish", false)

        return attachment
    }

    /**
     * Sometimes description contains unescaped html
     *
     * @param html stuff that needs to be formatted
     * @return String with the tags escaped
     */
    static String escapeHtml(CharSequence html) {
        return StringEscapeUtils.escapeHtml4(String.valueOf(html))
    }

    /**
     * @param playId
     * @return appropriate playstore Url
     */
    static String getUrlFromPlayId(String playId){
        return "https://play.google.com/store/apps/details?id=${playId}&hl=en&gl=us"
    }

}
