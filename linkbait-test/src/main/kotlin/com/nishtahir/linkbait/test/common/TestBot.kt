package com.nishtahir.linkbait.test.common

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.AbstractBot
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.test.TestBotApplication
import com.nishtahir.linkbait.test.controller.Message
import javafx.collections.ListChangeListener
import kotlin.concurrent.thread

/**
 * Created by maxke on 23.08.2016.
 * Local bot impl
 */
class TestBot(config: Configuration): AbstractBot(config) {

    var mssgner: Messenger
    var lastmsg = ""

    init {
        eventBus = EventBus("local")
        val gui = TestBotApplication()
        thread { TestBotApplication.main(emptyArray()) }
        mssgner = TestMessenger(gui.mainController)

        gui.mainController.msgs.addListener(ListChangeListener<Message> { msg ->
            msg.next()
            val messageEvent = MessageEvent()
            messageEvent.message = msg.addedSubList[0].msg
            if (lastmsg == messageEvent.message) {
                return@ListChangeListener
            } else {
                lastmsg = messageEvent.message
            }
            messageEvent.channel = "local"
            messageEvent.isDirectMessage = false
            messageEvent.isDirectedAtBot = msg.addedSubList[0].msg.startsWith("@linkbait")
            if (messageEvent.isDirectedAtBot) {
                messageEvent.message = messageEvent.message.removePrefix("@linkbait ")
            }
            messageEvent.id = msg.addedSubList[0].time
            messageEvent.sender = "local_user"
            eventBus.post(messageEvent)
        })
    }

    override fun startUp() {
        super.startUp()
    }

    override fun  shutDown() {
        super.shutDown()
    }

    override fun run() {
        super.run()
        while (isRunning) {
            //Don't know if this is a good idea, but
            //it seemed wrong to let the process run in an
            //unmanaged infinite loop
            Thread.sleep(500)
        }
    }

    override fun setMessenger(messenger: Messenger) {
        mssgner = messenger
    }

    override fun getMessenger(): Messenger {
        return mssgner
    }

    override fun getPluginState() {
        // Here be Nutella
    }
}