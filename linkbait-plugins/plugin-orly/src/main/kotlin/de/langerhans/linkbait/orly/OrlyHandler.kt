package de.langerhans.linkbait.orly

import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import java.net.URL
import java.net.URLEncoder
import java.nio.file.Files
import java.util.*

/**
 * Created by maxke on 15.08.2016.
 * O RLY?
 */
class OrlyHandler(val context: PluginContext): MessageEventListener {

    private val BASE_URL = "https://dev.to/rly/generate?"

    fun String.urlEncode(): String = URLEncoder.encode(this, Charsets.UTF_8.name())

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot) {
            if (event.message.startsWith("orly ")) {
                val parts = event.message.substringAfter("orly ").split(";")
                if (parts.size < 3) {
                    context.getMessenger().sendMessage(event.channel, "Not enough parameters. Try `@linkbait orly Using Linkbait;The only bot you'll ever need;@linkbait`.")
                    return
                }

                val title = parts[0].urlEncode()
                val topText = parts[1].urlEncode()
                val author = parts[2].urlEncode()

                val rand = Random()
                val animal = (1..40).toList()[rand.nextInt(40)].toString()
                val theme = (0..16).toList()[rand.nextInt(17)].toString()

                val query = "title=$title&top_text=$topText&author=$author&image_code=$animal&theme=$theme&guide_text=The%20Definitive%20Guide&guide_text_placement=bottom_right"
                val finalUrl = BASE_URL + query

                val image = URL(finalUrl).readBytes()
                if (image.size > 0) {
                    val tempFile = Files.createTempFile("orly", ".png").toFile()
                    tempFile.writeBytes(image)
                    context.getMessenger().uploadFile(event.channel, tempFile)
                    tempFile.delete()
                } else {
                    context.getMessenger().sendMessage(event.channel, "I'm sorry, but something went wrong :disappointed:")
                }

            }
        }
    }

}