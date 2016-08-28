package de.langerhans.linkbait.tenor

import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import de.langerhans.linkbait.tenor.model.TenorError
import de.langerhans.linkbait.tenor.model.TenorResponse
import java.util.*

/**
 * Created by maxke on 24.08.2016.
 * Better gifs!
 */
class TenorHandler(val context: PluginContext): MessageEventListener {

    val service = TenorService.build()

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot) {
            if (event.message.startsWith("gif ")) {
                val query = event.message.substringAfter("gif ")
                if (query.isEmpty()) {
                    context.getMessenger().sendMessage(event.channel, "Not enough parameters. Try `@linkbait gif nutella")
                    return
                }

                val result = service.search(query).execute()
                if (!result.isSuccessful) {
                    context.getMessenger().sendMessage(event.channel, "An error occured :disappointed:")
                    return
                }

                val response = result.body()

                if (response is TenorError) {
                    context.getMessenger().sendMessage(event.channel, "An error occured :disappointed:")
                    return
                }

                if (response is TenorResponse) {
                    val all = response.results
                    if (all == null || all.size == 0 || all.first().media.size == 0) {
                        context.getMessenger().sendMessage(event.channel, "No gifs found :disappointed:")
                        return
                    }

                    Collections.shuffle(all)

                    val gif = all[0].media[0].gif

                    var title = all[0].title
                    if (title.isEmpty()) {
                        title = query
                    }

                    context.getMessenger().sendMessage(event.channel, "<${gif.url}|$title (Powered by Tenor)>") // Meh attribution
                }
            }
        }
    }

}