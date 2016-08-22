package co.jasonwyatt.linkbait.asciitext

import com.github.lalyos.jfiglet.FigletFont
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import java.util.regex.Pattern

/**
 * Created by jason on 8/22/16.
 */
class AsciiTextHandler(val context: PluginContext): MessageEventListener {
    private var msgPattern = Pattern.compile("asciitext (.*)")

    override fun handleMessageEvent(event: MessageEvent) {
        if (!event.isDirectedAtBot) {
            return
        }

        var matcher = msgPattern.matcher(event.message);
        if (!matcher.matches()) {
            return
        }

        var text = matcher.group(1)

        context.getMessenger().sendMessage(event.channel, "```"+FigletFont.convertOneLine(text)+"```")
    }
}
