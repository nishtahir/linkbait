package de.langerhans.linkbait.imdb

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.PluginContext
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import org.jetbrains.spek.api.Spek
import java.io.File

/**
 * Created by maxke on 24.04.2016.
 * Tests
 */
class ImdbHandlerSpec() : Spek({

    describe("ImbdPlugin") {
//
//        given("parse method") {
//            val handler = ImdbHandler()
//            val sessionId = "linkbait"
//            on("Valid command") {
//                val movies = listOf("some movie", "movie", "123")
//                it("should return a result") {
//                    movies.forEach { movie ->
//                        assertEquals(movie, handler.parse("<@$sessionId>: imdb $movie", sessionId))
//                    }
//                }
//            }
//
//            on("Invalid session") {
//                val movie = "some movie"
//                it("should fail with an exception") {
//                    assertFailsWith(RequestParseException::class) {
//                        handler.parse("<@potato>: imdb $movie", sessionId)
//                    }
//                }
//            }
//
//            on("Invalid command") {
//                val movie = "some movie"
//                it("should fail with an exception") {
//                    assertFailsWith(RequestParseException::class) {
//                        handler.parse("<@potato>: potato $movie", sessionId)
//                    }
//                }
//            }
//        }
//
    }

})


/**
 * Mock the components needed to test the handler
 */
class MockContext : PluginContext {

    val messenger = MockMessenger()

    override fun getPluginState() {
    }

    override fun getMessenger(): Messenger {
        return messenger
    }

    override fun getConfiguration(): Configuration {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerListener(listener: EventListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterListener(listener: EventListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class MockMessenger : Messenger {

    var attachment: Attachment? = null

    override fun sendMessage(channel: String, message: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendAttachment(channel: String, attachment: Attachment) {
        this.attachment = attachment
    }

    override fun uploadFile(channel: String, file: File) {
        throw UnsupportedOperationException()
    }

}