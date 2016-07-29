package com.nishtahir.linkbait.memegen

import com.nishtahir.MemeGenerator
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream
import java.util.*


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
            "success" -> return getImageFile("success_kid.jpg")
            "ggg" -> return getImageFile("good_guy_greg.jpg")
            "ss" -> return getImageFile("scumbag_steve.jpg")
            "cat" -> return getImageFile("grumpy_cat.jpg")
            "morpheus" -> return getImageFile("what_if_i_told_you.jpg")
            "roman" -> return getImageFile("downvoting_roman.jpg")
        }
        return null
    }

    fun getImageFile(path: String): File {
        return copyInputStreamToTempFile(javaClass.classLoader.getResourceAsStream(path))
    }

    fun copyInputStreamToTempFile(inputStream: InputStream): File {
        val tempFile: File = File.createTempFile(UUID.randomUUID().toString(), "jpg");
        tempFile.deleteOnExit();
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile
    }

}