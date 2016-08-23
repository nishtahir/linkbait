package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.plugin.*
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import org.jetbrains.spek.api.Spek
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class HeySnackFoodPluginSpec : Spek({

    val SNACK_FOOD_NAME = ":nutella:"

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:test.sqlite"))
    InjektModule.scope.addFactory { HeySnackFoodService() }

    val context: PluginContext = MockContext()

    val handler: HeySnackFoodHandler = HeySnackFoodHandler(context)
    val messenger: MockMessenger = context.getMessenger() as MockMessenger

    describe("a message event ") {
        val service: HeySnackFoodService = InjektModule.scope.get()

        given("a wild $SNACK_FOOD_NAME event") {

            beforeEach {
                TableUtils.clearTable(InjektModule.scope.get<JdbcConnectionSource>(), User::class.java)
                messenger.message = ""
            }

            it("should return a heart emoji when given nuttela") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "something something $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertTrue (messenger.message.contains(":heart:", true))
            }

            it("should reward one nuttela correctly"){
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser("user").count)
            }

            it("should reward many many nuttela correctly"){
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME $SNACK_FOOD_NAME $SNACK_FOOD_NAME "

                handler.handleMessageEvent(messageEvent)
                assertEquals(3, service.findOrCreateUser("user").count)
            }

            it("should reward not reward unknown snacks"){
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user :eggplant:"

                handler.handleMessageEvent(messageEvent)
                assertEquals(0, service.findOrCreateUser("user").count)
            }

            it("should reward $SNACK_FOOD_NAME withing context strings"){
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user: sometimes people saythings and give $SNACK_FOOD_NAME and :taco:"

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser("user").count)
            }

            it("should not reward $SNACK_FOOD_NAME in direct messages") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = true
                messageEvent.channel = "test"
                messageEvent.sender = "notUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(0, service.findOrCreateUser("user").count)
            }

            it("should not allow users to reward themselves $SNACK_FOOD_NAME") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.channel = "test"
                messageEvent.sender = "user"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(0, service.findOrCreateUser("user").count)
            }
        }

        given("A $SNACK_FOOD_NAME given to a user"){

            beforeEach {
                TableUtils.clearTable(InjektModule.scope.get<JdbcConnectionSource>(), User::class.java)
                messenger.message = ""

                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "notUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser("user").count)
            }


//            it("should return the correct content in the leaderboard"){
//
//                val messageEvent: MessageEvent = MessageEvent()
//                messageEvent.isDirectedAtBot = true
//                messageEvent.channel = "test"
//                messageEvent.sender = "user"
//                messageEvent.message = "leaderboard"
//
//                handler.handleMessageEvent(messageEvent)
////                TODO("Add checks here")
////                println (messenger.message)
//            }
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

    var message: String = ""

    override fun sendMessage(channel: String, message: String) {
        this.message = message
    }

    override fun addReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendAttachment(channel: String, attachment: Attachment) {
        throw UnsupportedOperationException()
    }

    override fun uploadFile(channel: String, file: File) {
        throw UnsupportedOperationException()
    }

    override fun getMessageFormatter(): MessageFormatter {
        throw UnsupportedOperationException()
    }

}