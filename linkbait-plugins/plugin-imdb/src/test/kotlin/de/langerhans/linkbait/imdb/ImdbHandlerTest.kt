package de.langerhans.linkbait.imdb

import com.nishtahir.linkbait.core.exception.RequestParseException
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Created by maxke on 24.04.2016.
 * Tests
 */
class ImdbHandlerTest(): Spek() {
    init {
        given("parse method") {
            val handler = ImdbHandler()
            val sessionId = "linkbait"
            on("Valid command") {
                val movies = listOf("some movie", "movie", "123")
                it("should return a result") {
                    movies.forEach { movie ->
                        assertEquals(movie, handler.parse("<@$sessionId>: imdb $movie", sessionId))
                    }
                }
            }

            on("Invalid session") {
                val movie = "some movie"
                it("should fail with an exception") {
                    assertFailsWith(RequestParseException::class) {
                        handler.parse("<@potato>: imdb $movie", sessionId)
                    }
                }
            }

            on("Invalid command") {
                val movie = "some movie"
                it("should fail with an exception") {
                    assertFailsWith(RequestParseException::class) {
                        handler.parse("<@potato>: potato $movie", sessionId)
                    }
                }
            }
        }
    }
}