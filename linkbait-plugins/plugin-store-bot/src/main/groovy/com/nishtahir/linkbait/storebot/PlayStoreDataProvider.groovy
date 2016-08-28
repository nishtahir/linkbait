package com.nishtahir.linkbait.storebot

import com.nishtahir.linkbait.plugin.Attachment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Gets and parses stuff from the Google playstore
 */
class PlayStoreDataProvider extends StoreDataProvider {

    PlayStoreDataProvider(String url) {
        super(url)
    }

    Map<String, String> getAppDetailsFromPlayStore() {
        String html = getEN_US_RegionalUrlFromPlayId(getPlayStoreId(url)).toURL().text

        Document doc = Jsoup.parse(html)

        def fields = [:]

        fields.put('title', doc.select('.document-title div').text().trim())
        fields.put('author', doc.select('div[itemprop=author] span[itemprop=name]').text().trim())

        fields.put('desc', makeFancyDescription(doc.select('div[itemprop=description]')
                .text().take(MAX_DESC_LENGTH).toString()))

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

    @Override
    Attachment getAttachment() {
        Map<String, String> appInfo = getAppDetailsFromPlayStore()

        def title = appInfo['title']
        def titleUrl = appInfo['title']
        def thumbUrl = appInfo['imageUrl']
        def color = '3F51B5'
        def body = appInfo['desc']

        Map additionalFields = [:]
        additionalFields.put("Author", appInfo['author'])
        additionalFields.put("Price", appInfo['price'])
        additionalFields.put("Last updated", appInfo['lastUpdated'])
        additionalFields.put("Downloads", appInfo['dlCount'])
        additionalFields.put("All hail his Majesty", "@nish")

        Attachment attachment = new Attachment(title, body, color, titleUrl, thumbUrl, "")
        attachment.additionalFields = additionalFields;
        return attachment
    }

    /**
     * Need to convert URL to en us region to that the
     * information comes out in english
     * @param playId
     * @return
     */
    static String getEN_US_RegionalUrlFromPlayId(String playId){
        return "https://play.google.com/store/apps/details?id=${playId}&hl=en&gl=us"
    }

}
