package com.nishtahir.linkbait.memegen

import com.nishtahir.MemeGenerator
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream


/**
 *  Memegen bot. Example command
 *  @linkbait memegen success; memegen handler; works;
 */
class MemegenHandler(val context: PluginContext) : MessageEventListener {

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot) {
            if (event.message.matches("""memegen\s+(?<text>.*)""".toRegex())) {
                val text = event.message.replaceFirst("memegen", "").split(";")
                val meme = createMeme(text[0].trim(), text[1].trim().orEmpty(), text[2].trim().orEmpty())
                meme?.let {
                    context.getMessenger().uploadFile(event.channel, meme)
                }
            }
        }
    }

    fun createMeme(title: String, top: String, bottom: String): File? {
        val file = getImageFromTitle(title)
        file?.let {
            val meme = File("memegen/${top.replace("\\s", "_")}-${bottom.replace("\\s", "_")}.jpg")
            meme.mkdirs()
            return MemeGenerator.generateMeme(it, meme, top, bottom)
        }

        return null
    }

    fun getImageFromTitle(title: String): File? {
        when (title) {
            "success" -> return copyInputStreamToTempFile(javaClass.classLoader.getResourceAsStream("success_kid.jpg"), title)
        }
        return null
    }

    fun copyInputStreamToTempFile(inputStream: InputStream, name: String): File {
        val tempFile: File = File.createTempFile(name, "jpg");
        tempFile.deleteOnExit();
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile
    }

}