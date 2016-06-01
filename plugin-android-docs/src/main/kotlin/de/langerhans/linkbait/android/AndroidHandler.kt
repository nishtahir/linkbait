package de.langerhans.linkbait.android

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.MessageRequestHandler
import com.ullink.slack.simpleslackapi.SlackAttachment
import com.ullink.slack.simpleslackapi.SlackPreparedMessage
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import java.net.URL

/**
 * Created by maxke on 01.06.2016.
 * Searches the android docs
 */

class AndroidHandler(): MessageRequestHandler() {
    override fun parse(message: String, sessionId: String): String {
        super.parse(message, sessionId)
        val pattern = """android(:?\s*):?([\w ]+)""".toRegex()
        return pattern.find(message)?.groups?.get(2)?.value ?: throw RequestParseException("This message wasn't aimed at the bot.")
    }

    override fun handle(session: SlackSession, event: SlackMessagePosted): Boolean {
        val input = parse(event.messageContent, session.sessionPersona().id).replace(" ", "+")
        val json = jacksonObjectMapper()

        val search = URL("https://content.googleapis.com/customsearch/v1?cx=000521750095050289010%3Azpcpi1ea4s8&key=AIzaSyCFhbGnjW06dYwvRCU8h_zjdpS4PYYbEe8&q=$input&start=1&num=3&hl=en&fields=searchInformation,items(title,link,snippet)").readText()
        val searchJson = json.readValue(search, SearchResult::class.java)

        if (searchJson.searchInformation.totalResults == "0") {
            session.sendMessage(event.channel, "No results!", null)
            return true
        }

        val transform = {item: SearchItem ->
            ":droid: *<${item.link}|${item.title.replace(" | Android Developers", "")}>*: _${item.snippet.replace("\n", " ")}_"
        }
        val text = searchJson.items.joinToString("\n", transform = transform)

        val msg = SlackPreparedMessage.Builder().withUnfurl(false).withMessage(text).build()
        session.sendMessage(event.channel, msg)
        return true
    }
}