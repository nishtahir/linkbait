package de.langerhans.linkbait.android

import com.nishtahir.linkbait.core.exception.RequestParseException
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Created by maxke on 01.06.2016.
 * Tests
 */
class AndroidHandlerTest(): Spek() {
    init {
        given("parse method") {
            val handler = AndroidHandler()
            val sessionId = "linkbait"
            on("Valid command") {
                val terms = listOf("some class", "class", "123")
                it("should return a result") {
                    terms.forEach { term ->
                        assertEquals(term, handler.parse("<@$sessionId>: android $term", sessionId))
                    }
                }
            }

            on("Invalid session") {
                val term = "some class"
                it("should fail with an exception") {
                    assertFailsWith(RequestParseException::class) {
                        handler.parse("<@potato>: android $term", sessionId)
                    }
                }
            }

            on("Invalid command") {
                val term = "some class"
                it("should fail with an exception") {
                    assertFailsWith(RequestParseException::class) {
                        handler.parse("<@potato>: potato $term", sessionId)
                    }
                }
            }
        }
    }
}