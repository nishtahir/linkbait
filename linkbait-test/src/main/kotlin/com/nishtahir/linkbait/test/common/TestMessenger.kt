package com.nishtahir.linkbait.test.common

/**
 * Created by nish on 10/1/16.
 */
import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageBuilder
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.test.controller.Message
import com.nishtahir.linkbait.test.controller.TestBotController
import tornadofx.resizeColumnsToFitContent
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by maxke on 23.08.2016.
 * Local messenger
 */
class TestMessenger(val controller: TestBotController) : Messenger {

    override fun sendMessage(channel: String, message: String, unfurl: Boolean) {
        val now = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        controller.msgs.add(Message(now, "linkbait", message))
        controller.screen.tblMessages.resizeColumnsToFitContent()
    }

    override fun sendDirectMessage(user: String, message: String) {
        throw UnsupportedOperationException()
    }

    override fun setChannelTopic(channel: String, topic: String) {
        throw UnsupportedOperationException()
    }

    override fun setTyping(channel: String) {
        throw UnsupportedOperationException()
    }

    override fun getMessageBuilder(): MessageBuilder {
        throw UnsupportedOperationException()
    }

    override fun addReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeReaction(channel: String, messageId: String, reaction: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendAttachment(channel: String, attachment: Attachment) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadFile(channel: String, file: File) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}