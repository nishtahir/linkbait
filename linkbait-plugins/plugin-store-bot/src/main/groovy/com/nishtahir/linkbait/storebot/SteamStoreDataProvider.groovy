package com.nishtahir.linkbait.storebot

import com.github.goive.steamapi.SteamApi
import com.github.goive.steamapi.SteamApiFactory
import com.github.goive.steamapi.data.SteamApp
import com.github.goive.steamapi.enums.CountryCode
import com.nishtahir.linkbait.plugin.Attachment

/**
 * Created by nish on 8/29/16.
 */
class SteamStoreDataProvider extends StoreDataProvider {

    SteamStoreDataProvider(String url) {
        super(url)
    }

    /**
     * Parse steam store app content.
     * @param id application id in steam store. Usually games.
     * @return Map of interesting content.
     */
    static Map<String, String> getAppDetailsFromSteam(long id) {
        SteamApi steamApi = SteamApiFactory.createSteamApi(CountryCode.US)

        try {
            SteamApp steamApp = steamApi.retrieveApp(id)

            def fields = [:]

            fields.put('title', steamApp.name)
            fields.put('publishers', steamApp.publishers.join(",").take(30))
            fields.put('desc', steamApp.aboutTheGame)
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

        } catch (Exception e) {
            e.printStackTrace()
        }

        return null
    }

    @Override
    Attachment getAttachment() {
        Map<String, String> appInfo = getAppDetailsFromSteam(getSteamId(url))

        def title = appInfo['title']
        def titleUrl = appInfo['title']
        def thumbUrl = appInfo['imageUrl']
        def color = '3F51B5'
        def body = appInfo['desc']

        Map additionalFields = [:]

        additionalFields.put("Publishers", appInfo['publishers'])
        additionalFields.put("Price", appInfo['price'])
        additionalFields.put("Availability", appInfo['availability'])
        additionalFields.put("Gaben's got nothing on", "@nish")

        Attachment attachment = new Attachment(title, body, color, titleUrl, thumbUrl, "")
        attachment.additionalFields = additionalFields;
        return attachment
    }
}
