package com.nishtahir.islackbot.util

import com.github.goive.steamapi.SteamApi
import com.github.goive.steamapi.SteamApiFactory
import com.github.goive.steamapi.data.SteamApp
import com.github.goive.steamapi.enums.CountryCode
import com.ullink.slack.simpleslackapi.SlackAttachment
import org.apache.commons.lang3.StringEscapeUtils

import java.util.regex.Pattern

/**
 * Steam store madness
 */
class SteamAttachmentCreator extends AttachmentCreator {

    /**
     * Parse steam store app content
     * @param playId
     * @return
     */
    static Map<String, String> getAppDetailsFromSteam(long id) {
        SteamApi steamApi = SteamApiFactory.createSteamApi(CountryCode.US)
        SteamApp steamApp = steamApi.retrieveApp(id)

        def fields = [:]

        fields.put('title', steamApp.name)
        fields.put('publishers', steamApp.publishers.join(",").take(30))
        fields.put('desc', makeFancyDescription(steamApp.aboutTheGame))
        fields.put('imageUrl', steamApp.headerImage)
        fields.put('categories', steamApp.categories.description.join(","))

        List<String> availability = new ArrayList<>()
        //For some reason, the elvis operator treats this as if not
        !steamApp.availableForWindows ?: availability.add("Windows")
        !steamApp.availableForLinux ?: availability.add("Linux")
        !steamApp.availableForMac ?: availability.add("Mac")

        fields.put('availability', availability.join(","))
        fields.put('price', steamApp.price.currency.symbol + steamApp.price.finalPrice)

        return fields
    }

    /**
     * {@inheritDoc }
     */
    @Override
    SlackAttachment getSlackAttachmentForUrl(String url) {
        Map<String, String> appInfo = getAppDetailsFromSteam(ValidationUtils.getSteamId(url))

        SlackAttachment attachment = new SlackAttachment()
        attachment.fallback = appInfo['title']
        attachment.title = appInfo['title']
        attachment.thumb_url = appInfo['imageUrl']
        attachment.color = '3F51B5' //Material Indigo 500
        attachment.text = appInfo['desc']
        attachment.addField("Publishers", appInfo['publishers'], true)
        attachment.addField("Price", appInfo['price'], true)
        attachment.addField("Availability", appInfo['availability'], true)
        attachment.addField("Gaben's got nothing on", "@nish", false)
        attachment.markdown_in = ['text']

        return attachment
    }
}
