package de.langerhans.linkbait.android

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import java.net.URL

/**
 * Created by maxke on 01.06.2016.
 * Searches the android docs
 */

class AndroidHandler(val context: PluginContext): MessageEventListener {

    val pattern = """android(:?\s*):?([\w ]+)""".toRegex()

    override fun handleMessageEvent(event: MessageEvent) {
        if(!event.isDirectedAtBot) return
        if (!pattern.matches(event.message)) return

        val input = pattern.find(event.message)?.groups?.get(2)?.value?.replace(" ", "%20")
        println("input : " + input)

        val json = jacksonObjectMapper()



        val search = URL("https://content.googleapis.com/customsearch/v1?cx=000521750095050289010%3Azpcpi1ea4s8&key=AIzaSyCFhbGnjW06dYwvRCU8h_zjdpS4PYYbEe8&q=$input&start=1&num=3&hl=en&fields=searchInformation,items(title,link,snippet)").readText()
        val searchJson = json.readValue(search, SearchResult::class.java)

        if (searchJson.searchInformation.totalResults == "0") {
            context.getMessenger().sendMessage(event.channel, "No results!")
            return
        }



        val transform = {item: SearchItem ->
            ":droid: *<${item.link}|${item.title.replace(" | Android Developers", "")}>*: _${item.snippet.replace("\n", " ")}_"
        }
        val text = searchJson.items.joinToString("\n", transform = transform)

        context.getMessenger().sendMessage(event.channel, text, unfurl = false)
    }
}