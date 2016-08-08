package com.nishtahir.linkbait

import com.beust.jcommander.JCommander
import com.nishtahir.linkbait.cli.CommandBot
import com.nishtahir.linkbait.cli.CommandLinkbait
import com.nishtahir.linkbait.cli.CommandServer
import com.nishtahir.linkbait.core.SlackBot
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

/**
 * Logger
 */
val LOG: Logger = LoggerFactory.getLogger(Application::class.java)

/**
 *  Entry point for app.
 *  Much magic here
 */
class Application {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            parseCommandLine(args)
            onShutdown()
            run()
        }

    }

}

private fun parseCommandLine(args: Array<String>) {
    val linkbaitCmd = CommandLinkbait()
    val cmd = JCommander(linkbaitCmd)
    val cmdServer = CommandServer()
    val cmdBot = CommandBot()

    cmd.addCommand(cmdServer)
    cmd.addCommand(cmdBot)

    cmd.parse(*args)

    linkbaitCmd.help?.let {
        if (it) {
            cmd.usage()
            exitProcess(0)
        }
    }

    when (cmd.parsedCommand) {
        "server" -> startServer(cmdServer)
        "bot" -> startBot(cmdBot)
    }
}

fun startBot(cmdBot: CommandBot) {
    val bot = SlackBot(cmdBot.getConfiguration(), cmdBot.apiKey, cmdBot.id)
    bot.startAsync()
}

fun startServer(cmdServer: CommandServer) {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
}

/**
 * Do cleanup and other nice things here.
 */
fun onShutdown() {
    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        LOG.info("""

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                       Stopping Linkbait                        ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        """
        )
    }))
}

/**
 *
 */
private fun run() {

    LOG.info(
            """
 __       __  .__   __.  __  ___ .______        ___       __  .___________.
|  |     |  | |  \ |  | |  |/  / |   _  \      /   \     |  | |           |
|  |     |  | |   \|  | |  '  /  |  |_)  |    /  ^  \    |  | `---|  |----`
|  |     |  | |  . `  | |    <   |   _  <    /  /_\  \   |  |     |  |
|  `----.|  | |  |\   | |  .  \  |  |_)  |  /  _____  \  |  |     |  |
|_______||__| |__| \__| |__|\__\ |______/  /__/     \__\ |__|     |__|

            """
    )

    LOG.info("""

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                       Starting Linkbait                        ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        """
    )


}


