package de.langerhans.linkbait.imdb

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nishtahir.linkbait.plugin.*
import java.net.URL

val IMDB_PLUGIN_PATTERN = """imdb(:?\s*):?([\w ]+)""".toRegex()

class ImdbPlugin : LinkbaitPlugin() {

    var handler: ImdbListener? = null

    override fun start(context: PluginContext) {
        handler = ImdbListener(context)
        handler?.let {
            context.registerListener(handler as ImdbListener)
        }
    }

    override fun stop(context: PluginContext) {
        handler?.let {
            context.unregisterListener(handler as ImdbListener)
        }
    }

    override fun onPluginStateChanged() {
    }

}

class ImdbListener(ctx: PluginContext) : MessageEventListener {

    val context = ctx

    override fun handleMessageEvent(event: MessageEvent) {
        if (!event.isDirectedAtBot) {
            return
        }
        val input = IMDB_PLUGIN_PATTERN.find(event.message)?.groups?.get(2)?.value?.replace(" ", "%20")
        val json = jacksonObjectMapper()
        json.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        if (input.isNullOrEmpty()) return

        // First use the undocumented IMDb API to get an idea what the user was looking for
        val searchRequest = URL("http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=$input").openConnection()


        // Set a user agent or the IMDb API will give us shit, aka a 403
        searchRequest.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.22 Safari/537.36")
        val search = searchRequest.inputStream.bufferedReader().readText()
        val searchJson = json.readValue(search, ImdbResults::class.java)

        // Fallback through popular -> exact -> approx. Turns out exact is not so exact after all...
        val id: String?
        if (searchJson.title_popular?.first()?.id == null) {
            if (searchJson.title_exact?.first()?.id == null) {
                if (searchJson.title_approx?.first()?.id == null) {
                    context.getMessenger().sendMessage(event.channel, "No results for this query.")
                    return
                } else {
                    id = searchJson.title_approx?.first()?.id
                }
            } else {
                id = searchJson.title_exact?.first()?.id
            }
        } else {
            id = searchJson.title_popular?.first()?.id
        }

        // Now get some actual data about the best result
        val apiResult = URL("http://www.omdbapi.com/?i=$id&plot=short&r=json").readText()
        val movie = json.readValue(apiResult, OmdbResult::class.java)

        if (movie.Error != null) {
            context.getMessenger().sendMessage(event.channel, movie.Error)
            return
        }

        val genres = movie.Genre.split(",")
        var linkedGenres = ""
        genres.forEach { genre -> linkedGenres += "<http://www.imdb.com/genre/${genre.trim().toLowerCase()}|$genre>, " }
        linkedGenres = linkedGenres.dropLast(2) // ", "

        val additionalFields = mapOf(
                "Year" to "<http://www.imdb.com/year/${movie.Year}|${movie.Year}>",
                "Genre" to linkedGenres,
                "Metascore" to "<http://www.imdb.com/title/${movie.imdbID}/criticreviews|${movie.Metascore}>",
                "IMDb rating" to "<http://www.imdb.com/title/${movie.imdbID}/reviews|${movie.imdbRating}>",
                "Awards" to "<http://www.imdb.com/title/${movie.imdbID}/awards|${movie.Awards}>"
        )

        val attachment = Attachment(
                title = movie.Title,
                titleUrl = "http://www.imdb.com/title/${movie.imdbID}",
                body = movie.Plot,
                color = "",
                imageUrl = "",
                thumbnailUrl = "")

        attachment.additionalFields = additionalFields

        // Sadly IMDb prevents hotlinking of the poster, so no thumbnail for us :(

        context.getMessenger().sendAttachment(event.channel, attachment)
        return
    }
}