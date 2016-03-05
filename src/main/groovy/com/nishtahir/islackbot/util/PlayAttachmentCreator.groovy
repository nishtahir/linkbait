package com.nishtahir.islackbot.util

import com.ullink.slack.simpleslackapi.SlackAttachment
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Playstore-bot functionality
 */
class PlayAttachmentCreator extends AttachmentCreator {

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
        fields.put('desc', makeFancyDescription(doc.select('div[itemprop=description]').text().take(MAX_DESC_LENGTH).toString()))
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
     * {@inheritDoc }
     */
    @Override
    SlackAttachment getSlackAttachmentForUrl(String url) {
        String editedUrl = getUrlFromPlayId(ValidationUtils.getPlaystoreId(url))
        Map<String, String> appInfo = getAppDetailsFromPlayStore(editedUrl)

        SlackAttachment attachment = new SlackAttachment()
        attachment.fallback = appInfo['title']
        attachment.title = appInfo['title']
        attachment.titleLink = editedUrl
        attachment.thumb_url = appInfo['imageUrl']
        attachment.color = '3F51B5' //Material Indigo 500
        attachment.text = appInfo['desc']
        attachment.addField("Author", appInfo['author'], true)
        attachment.addField("Price", appInfo['price'], true)
        attachment.addField("Last updated", appInfo['lastUpdated'], true)
        attachment.addField("Downloads", appInfo['dlCount'], true)
        attachment.addField("All hail his Majesty", "@nish", false)
        attachment.markdown_in = ['text']

        return attachment
    }

    /**
     * @param playId
     * @return appropriate playstore Url
     */
    static String getUrlFromPlayId(String playId){
        return "https://play.google.com/store/apps/details?id=${playId}&hl=en&gl=us"
    }

}
