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
        val match = """[\s+]?@(?<user>\w+)[\s+]?""".toRegex().find(event.message)
        match?.let {
            val userName = it.value.trim().drop(1)

            if (event.isDirectMessage) {
                if (userName.equals(event.sender)) {
                    TODO("Add shame gif")
                    context.getMessenger().sendMessage(event.channel, "")
                } else {
                    context.getMessenger().sendMessage(event.channel, "Show your appreciation for @$userName and give :nutella in a public channel")
                }
                return
            }
            if (userName.equals(event.sender)) {
                context.getMessenger().sendMessage(event.channel, "Don't be greedy! Share the $snackFood love!")
                return
            }

            val sweetDeliciousNuttella = snackFood.toRegex().findAll(event.message)
            if (sweetDeliciousNuttella.count() > 0) {
                val user = service.findOrCreateUser(userName)
                user.incrementCount(sweetDeliciousNuttella.count())
                service.updateUser(user)
            }

        }

        if (!event.isDirectedAtBot) {
            return
        }

        if (event.message.trim() == "leaderboard") {
            val list = service.getLeaderboad()

            val leaderboard = StringBuilder()
            leaderboard.appendNew("* Hey $snackFood Leaderboard! * \n\n")
            list.forEachIndexed { i, user ->
                if (i > 10) {
                    return@forEachIndexed
                }
                leaderboard.appendNew("${i + 1}. ${user.name}    *${user.count}*  ${ if (i == 0) ":crown: \n" else ""}")
            }
            context.getMessenger().sendMessage(event.channel, leaderboard.toString())

        } else if (event.message.contains("$snackFood")) {
            context.getMessenger().sendMessage(event.channel, ":heart:")
        }
    }

}

