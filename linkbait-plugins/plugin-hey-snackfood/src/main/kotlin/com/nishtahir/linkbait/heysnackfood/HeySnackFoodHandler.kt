package com.nishtahir.linkbait.heysnackfood

import com.google.common.eventbus.Subscribe
import com.nishtahir.linkbait.plugin.Attachment
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.MessageEventListener
import com.nishtahir.linkbait.plugin.PluginContext
import uy.kohesive.injekt.Injekt


/**
 *  Handle the pokedex command
 */
class HeySnackFoodHandler(val context: PluginContext) : MessageEventListener {

    val service: HeySnackFoodService = InjektModule.scope.get()

    val snackFood = ":nutella:"

    @Subscribe
    override fun handleMessageEvent(event: MessageEvent) {
        val userMatches = """[\s+]?@(?<user>\w+)[\s+]?""".toRegex().find(event.message)
        if (userMatches != null && event.message.contains(snackFood)) {
            val snackfoods = snackFood.toRegex().findAll(event.message)

            if (snackfoods.count() > 0) {
                val user = service.findOrCreateUser(userMatches.value.trim().substring(1))
                user.incrementCount(snackfoods.count())
                service.updateUser(user)
            }
        }

        if (event.message.trim() == "leaderboard") {
            val list = service.getLeaderboad()

            context.getMessenger().sendMessage(event.channel, "*Leaderboard*")
            list.forEachIndexed { i, user ->
                if (i > 10) {
                    return@forEachIndexed
                }
                context.getMessenger().sendMessage(event.channel, "${i + 1}. ${user.name}    *${user.count}*")
            }
        }
    }

}