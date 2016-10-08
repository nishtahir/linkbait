package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.plugin.*
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class HeySnackFoodPluginSpec : Spek({

    val SNACK_FOOD_NAME = ":nutella:"

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:test.sqlite"))
    InjektModule.scope.addFactory { HeySnackFoodService() }

    val mockConfig = MockConfiguration()
    val mockMessenger = MockMessenger()
    val mockContext = MockContext(mockConfig, mockMessenger)

    val handler: HeySnackFoodHandler = HeySnackFoodHandler(mockContext)

    describe("a message event ") {
        val service: HeySnackFoodService = InjektModule.scope.get()

        given("a wild $SNACK_FOOD_NAME event") {

            beforeEach {
                TableUtils.clearTable(InjektModule.scope.get<JdbcConnectionSource>(), User::class.java)
                mockMessenger.message = ""
            }

            it("should return a heart emoji when given $SNACK_FOOD_NAME") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "something something $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertTrue (mockMessenger.message.contains(":heart:", true))
            }

            it("should reward one $SNACK_FOOD_NAME correctly") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser("user").count)
            }

            it("should reward one $SNACK_FOOD_NAME to user with a dot in name") {
                val user = "test.user"
                val messageEvent: MessageEvent = MessageEvent().apply {
                    isDirectedAtBot = false
                    isDirectMessage = false
                    channel = "test"
                    sender = "other.user"
                    message = "@$user $SNACK_FOOD_NAME"
                }

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser(user).count)
            }

            it("should reward many many $SNACK_FOOD_NAME correctly") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME $SNACK_FOOD_NAME $SNACK_FOOD_NAME "

                handler.handleMessageEvent(messageEvent)
                assertEquals(3, service.findOrCreateUser("user").count)
            }

            it("should reward not reward unknown snacks") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "testUser"
                messageEvent.message = "@user :eggplant:"

                handler.handleMessageEvent(messageEvent)
                assertEquals(0, service.findOrCreateUser("user").count)
            }

            it("should reward $SNACK_FOOD_NAME withing context strings") {
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

        given("A $SNACK_FOOD_NAME given to a user") {

            beforeEach {
                TableUtils.clearTable(InjektModule.scope.get<JdbcConnectionSource>(), User::class.java)
                mockMessenger.message = ""

                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = false
                messageEvent.isDirectMessage = false
                messageEvent.channel = "test"
                messageEvent.sender = "notUser"
                messageEvent.message = "@user $SNACK_FOOD_NAME"

                handler.handleMessageEvent(messageEvent)
                assertEquals(1, service.findOrCreateUser("user").count)
            }
        }
    }
})