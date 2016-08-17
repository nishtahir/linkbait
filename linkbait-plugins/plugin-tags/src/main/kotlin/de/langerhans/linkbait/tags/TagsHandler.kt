package de.langerhans.linkbait.tags

import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * Created by maxke on 16.08.2016.
 * Tags message handler
 */
class TagsHandler(val context: PluginContext): MessageEventListener {

    val service: TagService = InjektModule.scope.get()

    private val HELP = """
Help for the tag command:
```
Add a tag      - @linkbait tag add <tag_name> <tag_value>
Show a tag     - @linkbait tag <tag_name>
Delete a tag   - @linkbait tag del <tag_name>
List all tags  - @linkbait tag list
```
Tags are saved on a per channel basis.
    """

    private val TAG_LIMIT = 25
    private val VALUE_LIMIT = 500

    private val updateTimers = HashMap<String, Triple<String, String, Timer>>()

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot && !event.isDirectMessage) {
            if (!event.message.startsWith("tag")) {
                return // Not for us
            }

            val parts = event.message.split(" ")

            if (parts.size < 2) {
                context.getMessenger().sendMessage(event.channel, HELP) // Must be not enough params
                return
            }

            when (parts[1]) {
                "add" -> {
                    if (parts.size < 4) {
                        context.getMessenger().sendMessage(event.channel, "Not enough parameters! Usage: `@linkbait tag add <tag_name> <tag_value>`")
                        return
                    }
                    addTag(event, parts[2], parts.drop(3).joinToString(" "))
                }
                "del" -> {
                    if (parts.size < 3) {
                        context.getMessenger().sendMessage(event.channel, "Not enough parameters! Usage: `@linkbait tag del <tag_name>`")
                        return
                    }
                    delTag(event, parts[2])
                }
                "list" -> allTags(event)
                else -> printTag(event, parts[1])
            }
        } else if (updateTimers.isNotEmpty() && event.message.matches("""y|yes|yep|sure|ok|okay""".toRegex(RegexOption.IGNORE_CASE))) {
            // User said yes
            if (updateTimers.containsKey(event.sender)) {
                val timer = updateTimers.remove(event.sender)
                timer?.third?.cancel()

                val tag = timer?.first
                val value = timer?.second

                if (tag != null && value != null) {
                    service.saveTag(event.channel, event.sender, tag, value)
                    context.getMessenger().sendMessage(event.channel, ":white_check_mark: Tag *$tag* updated!")
                }

            }
        } else if (updateTimers.isNotEmpty() && event.message.matches("""n|no|nope""".toRegex(RegexOption.IGNORE_CASE))) {
            // User said no, cancel the timer, let them know and clear it from the list
            val timer = updateTimers.remove(event.sender)
            timer?.third?.cancel() ?: return
            context.getMessenger().sendMessage(event.channel, ":x: Tag *${timer?.first ?: ""}* not updated!")
        }
    }

    private fun allTags(event: MessageEvent) {
        val tags = service.allTagsByChannel(event.channel)
        if (tags.size == 0) {
            context.getMessenger().sendMessage(event.channel, ":x: No tags found!")
            return
        }

        val msg = tags.joinToString(", ", "Tags for this channel:\n>>>", transform = {tag -> tag.tag})
        context.getMessenger().sendMessage(event.channel, msg)
    }

    private fun printTag(event: MessageEvent, tag: String) {
        val thisTag = service.findTagByName(event.channel, tag)
        if (thisTag == null) {
            context.getMessenger().sendMessage(event.channel, ":x: Tag *$tag* not found!")
            return
        }

        context.getMessenger().sendMessage(event.channel, ">>>${thisTag.value}")
    }

    private fun delTag(event: MessageEvent, tag: String) {
        val success = service.deleteTagByName(event.channel, tag)
        if (success) {
            context.getMessenger().sendMessage(event.channel, ":trashcan: Tag *$tag* deleted!")
        } else {
            context.getMessenger().sendMessage(event.channel, ":x: Tag *$tag* not found!")
        }
    }

    private fun addTag(event: MessageEvent, tag: String, value: String) {
        if (tag.length > TAG_LIMIT) {
            context.getMessenger().sendMessage(event.channel, ":x: Tag name can be at most $TAG_LIMIT characters long!")
            return
        }

        if (value.length > VALUE_LIMIT) {
            context.getMessenger().sendMessage(event.channel, ":x: Tag value can be at most $VALUE_LIMIT characters long!")
            return
        }

        val maybeTag = service.findTagByName(event.channel, tag)
        if (maybeTag != null) {
            if (maybeTag.value == value) {
                // Same tag, same content
                context.getMessenger().sendMessage(event.channel, ":exclamation: Tag *$tag* exists already and has the same value.")
                return
            }

            // We give the user 10 seconds to decide what to do.
            val timer = fixedRateTimer(period = 10000, initialDelay = 10000) {
                context.getMessenger().sendMessage(event.channel, ":x: Tag $tag not updated!")
                cancel()
            }
            updateTimers.put(event.sender, Triple(tag, value, timer))
            context.getMessenger().sendMessage(event.channel, ":question: Tag *$tag* exists already, do you want to update it?\n>>>${maybeTag.value}")
        } else {
            // A new tag, yay!
            service.saveTag(event.channel, event.sender, tag, value)
            context.getMessenger().sendMessage(event.channel, ":white_check_mark: Tag *$tag* added!")
        }
    }

}