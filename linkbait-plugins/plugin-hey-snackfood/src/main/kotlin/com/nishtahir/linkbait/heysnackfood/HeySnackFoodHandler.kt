package com.nishtahir.linkbait.heysnackfood

import com.google.common.eventbus.Subscribe
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext


/**
 *  People show that they love each other by giving each other
 *  sweet delicious snacks. That's what makes the world go round.
 *
 *  True story...
 */
class HeySnackFoodHandler(val context: PluginContext) : MessageEventListener {

    val service: HeySnackFoodService = InjektModule.scope.get()

    val snackFood = ":nutella:"

    @Subscribe
    override fun handleMessageEvent(event: MessageEvent) {
        val match = """[\s+]?@(?<user>[\w|\.]+)[\s+]?""".toRegex().find(event.message)
        match?.let {
            val userName = it.value.trim().drop(1)

            if (event.isDirectMessage) {
                if (userName.equals(event.sender)) {
                    context.getMessenger().sendDirectMessage(event.sender, "<https://media.giphy.com/media/xTiTnmrpGi0zetc9Xy/giphy.gif|Shame. Shame. Shame.>")
                } else {
                    context.getMessenger().sendMessage(event.channel, "Show your appreciation for @$userName and give :nutella in a public channel")
                }
                return
            }
            if (userName.equals(event.sender) && event.message.contains(snackFood)) {
                context.getMessenger().sendMessage(event.channel, "Don't be greedy! Share the $snackFood love!")
                return
            }

            val sweetDeliciousNutella = snackFood.toRegex().findAll(event.message)
            if (sweetDeliciousNutella.count() > 0) {
                val user = service.findOrCreateUser(userName)
                user.incrementCount(sweetDeliciousNutella.count())
                service.updateUser(user)
            }

        }

        if (!event.isDirectedAtBot) {
            return
        }

        if (event.message.trim() == "leaderboard") {
            val list = service.getLeaderboad()

            val builder = context.getMessenger().getMessageBuilder()
            builder.bold("Hey $snackFood Leaderboard!")
            builder.text("\n\n")
            list.forEachIndexed { i, user ->
                if (i > 10) {
                    return@forEachIndexed
                }

                builder.text("${i + 1}. ${user.name}")
                builder.text("    ")
                builder.bold("${user.count}")
                if (i == 0) {
                    builder.text(":crown: \n")
                }
                builder.text("\n")
            }
            context.getMessenger().sendMessage(event.channel, builder.build())

        } else if (event.message.contains("$snackFood")) {
            context.getMessenger().sendMessage(event.channel, ":heart:")
        }
    }

}

