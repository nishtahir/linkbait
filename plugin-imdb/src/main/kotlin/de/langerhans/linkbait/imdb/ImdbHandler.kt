package de.langerhans.linkbait.imdb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nishtahir.linkbait.core.exception.RequestParseException
import com.nishtahir.linkbait.core.request.MessageRequestHandler
import com.ullink.slack.simpleslackapi.SlackAttachment
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import java.net.URL

/**
 * Created by maxke on 24.04.2016.
 * Gets info about movies from
 */

class ImdbHandler(): MessageRequestHandler() {
    override fun parse(message: String, sessionId: String): String {
        super.parse(message, sessionId)
        val pattern = """imdb(:?\s*):?([\w ]+)""".toRegex()
        return pattern.find(message)?.groups?.get(2)?.value ?: throw RequestParseException("This message wasn't aimed at the bot.")
    }

    override fun handle(session: SlackSession, event: SlackMessagePosted): Boolean {
        val input = parse(event.messageContent, session.sessionPersona().id).replace(" ", "%20")
        val json = jacksonObjectMapper()

        // First use the undocumented IMDb API to get an idea what the user was looking for
        val searchRequest = URL("http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=$input").openConnection()

        // Set a user agent or the IMDb API will give us shit, aka a 403
        searchRequest.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.22 Safari/537.36")
        val search = searchRequest.inputStream.bufferedReader().readText()
        val searchJson = json.readValue(search, ImdbResults::class.java)

        // Fallback through popular -> exact -> approx. Turns out exact is not so exact after all...
        var id: String?
        if (searchJson.title_popular?.first()?.id == null) {
            if (searchJson.title_exact?.first()?.id == null) {
                if (searchJson.title_approx?.first()?.id == null) {
                    session.sendMessage(event.channel, "No results for this query.", null)
                    return true
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
            session.sendMessage(event.channel, movie.Error, null)
            return true
        }

        val attachment = SlackAttachment()
        attachment.title = movie.Title
        attachment.titleLink = "http://www.imdb.com/title/${movie.imdbID}"
        attachment.fallback = movie.Title
        attachment.text = movie.Plot
        attachment.addField("Year", "<http://www.imdb.com/year/${movie.Year}|${movie.Year}>", true)

        val genres = movie.Genre.split(",")
        var linkedGenres = ""
        genres.forEach { genre -> linkedGenres += "<http://www.imdb.com/genre/${genre.trim().toLowerCase()}|$genre>, " }
        linkedGenres = linkedGenres.dropLast(2) // ", "
        attachment.addField("Genre", linkedGenres, true)

        attachment.addField("Metascore", "<http://www.imdb.com/title/${movie.imdbID}/criticreviews|${movie.Metascore}>", true)
        attachment.addField("IMDb rating", "<http://www.imdb.com/title/${movie.imdbID}/reviews|${movie.imdbRating}>", true)
        attachment.addField("Awards", "<http://www.imdb.com/title/${movie.imdbID}/awards|${movie.Awards}>", true)
        // Sadly IMDb prevents hotlinking of the poster, so no thumbnail for us :(

        session.sendMessage(event.channel, "", attachment)

        return true
    }
}