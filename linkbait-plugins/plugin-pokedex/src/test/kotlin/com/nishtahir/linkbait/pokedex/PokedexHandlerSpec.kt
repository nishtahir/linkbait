package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.PluginContext
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import org.jetbrains.spek.api.Spek

import kotlin.test.assertEquals

class PokedexHandlerTest : Spek({

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:src/main/resources/linkbait-pokedex.sqlite"))
    InjektModule.scope.addFactory { PokedexService() }

    val context: PluginContext = MockContext()

    val handler: PokedexHandler = PokedexHandler(context)
    val messenger: MockMessenger = context.getMessenger() as MockMessenger

    describe("a message event ") {
        val service: PokedexService = InjektModule.scope.get()

        given("handle event") {

            beforeEach {
                messenger.attachment = null
            }

            it("should do nothing without the trigger word") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.channel = "test"
                messageEvent.message = "pokedex mewtwo"


                handler.handleMessageEvent(messageEvent)
                assertEquals(messenger.attachment, null)
            }

            it("should return correct pokemon when given a name") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "pokedex mewtwo"


                handler.handleMessageEvent(messageEvent)
                assertEquals(messenger.attachment?.title, "Mewtwo")
            }

            it("should return correct pokemon when given an id") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "pokedex 150"


                handler.handleMessageEvent(messageEvent)
                assertEquals(messenger.attachment?.title, "Mewtwo")
            }


            it("should return missingno when given an id that doesn't exist") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "pokedex 9999"


                handler.handleMessageEvent(messageEvent)
                assertEquals(messenger.attachment?.title, service.missingNo.name)
            }
        }

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

}