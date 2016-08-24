package com.nishtahir.linkbait.memegen

import com.nishtahir.MemeGenerator
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream
import java.net.URL
import java.util.*


/**
 *  Memegen bot. Example command
 *  @linkbait memegen success; memegen handler; works;
 */
class MemegenHandler(val context: PluginContext) : MessageEventListener {

    val templatesFile = File("data/templates")

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot) {
            if (!event.message.startsWith("memegen")) {
                //move along... nothing to see here
                return
            }

            val commands = event.message.split(" ")

            if (commands.size < 2) {
                context.getMessenger().sendMessage(event.channel, formatHelp())
                return
            }

            val text = event.message.replaceFirst(commands[0], "")
                    .replaceFirst(commands[1], "")
                    .trim()

            val params = text.split(";")
            val meme = createMeme(commands[1].trim(), params[0].orEmpty(), params[1].trim().orEmpty())
            meme?.let {
                context.getMessenger().uploadFile(event.channel, meme)
            }
        }
    }


    private fun formatHelp(): String {
        val memes = templatesFile.listFiles().joinToString(", ") {
            "`${it.name.dropLast(4)}`"
        }

        val messageBuilder = context.getMessenger().getMessageBuilder()
        messageBuilder.pre(
                """
Memegen usage: memegen [meme name] [top text];[bottom text]
Available memes:
"""
        ).text("\n")

        return messageBuilder.build() + memes
    }

    fun createMeme(title: String, top: String, bottom: String): File? {
        val outputMemeFile = File("data/memegen/${UUID.randomUUID().toString()}.jpg")
        val file = findImageFile(title)
        file?.let {
            outputMemeFile.mkdirs()
            return MemeGenerator.generateMeme(it, outputMemeFile, top, bottom)
        }

        return null
    }

    fun findImageFile(title: String): File? {
        val template = templatesFile.listFiles().firstOrNull() {
            it.name.dropLast(4) == title
        }
        return template
    }

}