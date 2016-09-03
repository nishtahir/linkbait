package com.nishtahir.linkbait.storebot

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import groovy.json.JsonSlurper
import org.jetbrains.annotations.NotNull

/**
 * Created by nish on 7/27/16.
 */
class StoreBotListener implements MessageEventListener {

    PluginContext context

    StoreBotListener(PluginContext context) {
        this.context = context
    }

    @Override
    void handleMessageEvent(@NotNull MessageEvent event) {
        String url = getUrlFromSlackLink(event.message)

        if (url == null) {
            return
        }

        StoreDataProvider provider = null
        if (url.contains("play.google.com")) {
            provider = new PlayStoreDataProvider(url)
        } else if (url.contains("store.steampowered.com")) {
            provider = new SteamStoreDataProvider((url))
        }

        if (provider != null) {
            context.messenger.sendAttachment(event.channel, provider.getAttachment())
        }
    }

    /**
     * Checks if URL is valid
     * @param context
     * @return true if valid
     */
    static boolean isValidUrl(String context) {
        try {
            URI.create(context)
            return true
        } catch (IllegalArgumentException ignored) {
            return false
        }
    }

    /**
     *  The assumption here is that slack has already done the url parsing.
     *  so we just need to extract the url.
     * @param context
     * @return
     */
    static String getUrlFromSlackLink(String context) {
        def matcher = (context =~ /(?i)<(?<url>(https?:\\/\\/)?([\da-z.-]+)\.([a-z.]{2,6})([\\/\w \-&?=%+.#]*)*\\/?)(|.*)?>/)
        if (matcher.find()) {
            return matcher.group('url')
        }

        return null
    }

}
