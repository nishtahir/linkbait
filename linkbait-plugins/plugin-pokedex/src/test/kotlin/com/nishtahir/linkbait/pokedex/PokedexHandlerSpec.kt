package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.plugin.*
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import java.io.File

import kotlin.test.assertEquals

class PokedexHandlerTest : Spek({

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:src/main/resources/linkbait-pokedex.sqlite"))
    InjektModule.scope.addFactory { PokedexService() }

    val config = MockConfiguration()
    val messenger = MockMessenger()
    val context = MockContext(config, messenger)

    val handler: PokedexHandler = PokedexHandler(context)

    describe("a message event ") {
        val service: PokedexService = InjektModule.scope.get()

        given("handle event") {

            beforeEach {
                messenger.reset()
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