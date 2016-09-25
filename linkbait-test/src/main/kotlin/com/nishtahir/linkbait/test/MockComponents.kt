package com.nishtahir.linkbait.test

import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageBuilder
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.PluginContext
import com.nishtahir.linkbait.plugin.model.Configuration
import com.nishtahir.linkbait.plugin.model.EventListener
import java.io.File

/**
 * Mock the components needed to test the handler
 */
open class MockContext(val config: Configuration, val message: Messenger) : PluginContext {

    override fun getPluginState() {
    }

    override fun getMessenger(): Messenger {
        return message
    }

    override fun getConfiguration(): Configuration {
        return config
    }

    override fun registerListener(listener: EventListener) {

    }

    override fun unregisterListener(listener: EventListener) {
    }

}

open class MockConfiguration : Configuration {
    override fun getStaticFileDirectory(): File {
        throw UnsupportedOperationException()
    }

    override fun getPluginDirectory(): File {
        throw UnsupportedOperationException()
    }

    override fun getTemporaryFileDirectory(): File {
        throw UnsupportedOperationException()
    }

    override fun getPluginRepository(): File {
        throw UnsupportedOperationException()
    }

    override fun getDataDirectory(): File {
        throw UnsupportedOperationException()
    }

}

open class MockMessenger : Messenger {
    var message: String = ""

    override fun sendMessage(channel: String, message: String, unfurl: Boolean) {
        this.message = message
    }

    override fun setTyping(channel: String) {
    }

    override fun setChannelTopic(channel: String, topic: String) {
    }

    override fun sendDirectMessage(user: String, message: String) {
    }

    override fun addReaction(channel: String, messageId: String, reaction: String) {
    }

    override fun removeReaction(channel: String, messageId: String, reaction: String) {
    }

    override fun sendAttachment(channel: String, attachment: Attachment) {
    }

    override fun uploadFile(channel: String, file: File) {
    }

    override fun getMessageBuilder(): MessageBuilder {
        throw UnsupportedOperationException("Not yet Implemented.")
    }

}