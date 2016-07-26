package com.nishtahir.linkbait.reddit

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import groovy.json.JsonSlurper
import org.jetbrains.annotations.NotNull

/**
 * Created by nish on 7/27/16.
 */
class RedditAutoCompleteListener implements MessageEventListener {

    PluginContext context

    RedditAutoCompleteListener(PluginContext context) {
        this.context = context
    }

    def USER_AGENT = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36'

    @Override
    void handleMessageEvent(@NotNull MessageEvent event) {
        def matcher = (event.message =~ /(^|\s+)\\/?(?<subreddit>(r\\/[\w|-]+))\\/?(\s+|\u0024)/)
        if (matcher.find()) {
            def subreddit = matcher.group('subreddit')
            def url = "https://www.reddit.com/$subreddit/"

            String response = "${url}about.json".toURL().getText(requestProperties: ['User-Agent': USER_AGENT])
            Map info = new JsonSlurper().parseText(response) as Map
            Map data = info.get("data") as Map

            if (data != null && !data.isEmpty()) {
                String title = subreddit
                String description = data.get("public_description")
                String color = "CCCCCC"

                String image = data.get("banner_img")
                String icon = data.get("icon_img")

                Map meta = ["Subcribers": data.get("subscribers").toString(),
                            "Title"     : data.get("title").toString()]
                Attachment attachment = new Attachment(title, description, color, url, icon, image)
                attachment.additionalFields = meta

                context.messenger.sendAttachment(event.channel, attachment)
            }
        }
    }

}
