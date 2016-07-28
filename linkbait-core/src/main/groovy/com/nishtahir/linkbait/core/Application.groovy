package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.plugin.model.Configuration

/**
 * Launcher to start the bot as a standalone app
 */
class Application {

    AbstractBot bot

    public Application(def args) {

        Runtime.addShutdownHook {
            println("""

Shutting down...

        """)
            bot != null && bot.stopAsync()
        }

        def cli = new CliBuilder(usage: '[opt] <args>')
        cli.with {
            h(longOpt: 'help', 'Print this message')

            _(longOpt: 'plugin-dir', 'Plugin directory', args: 1, required: false)
            _(longOpt: 'static-dir', 'Static file directory', args: 1, required: false)
            _(longOpt: 'temp-dir', 'Temporary file directory', args: 1, required: false)

            _(longOpt: 'id', 'Bot identifier', args: 1, required: true)
            _(longOpt: 'key', 'Bot api key or token', args: 1, required: true)
            _(longOpt: 'service', 'Service to connect to', args: 1, required: true)
        }
        def opt = cli.parse(args)
        if (!opt) return
        if (opt.h) cli.usage()

        println("${opt.getProperty("plugin-dir")}")

        Configuration configuration = new Configuration() {
            @Override
            File getStaticFileDirectory() {
                def staticDir = opt.getProperty("static-dir")
                return staticDir ? new File(staticDir as String) : new File("static")
            }

            @Override
            File getPluginDirectory() {
                def pluginDir = opt.getProperty("plugin-dir")
                return pluginDir ? new File(pluginDir as String) : new File("plugin")
            }

            @Override
            File getTemporaryFileDirectory() {
                def pluginDir = opt.getProperty("temp-dir")
                return pluginDir ? new File(pluginDir as String) : new File("temp")
            }

            @Override
            File getPluginRepository() {
                return new File("repo")
            }
        }

        String id = opt.getProperty("id")
        String apiKey = opt.getProperty("key")
        String service = opt.getProperty("service")

        println("""

Starting up bot...

        """)
        bot = new SlackBot(configuration, apiKey, id)
        bot.startAsync()
    }

    static void main(def args) {
        new Application(args)
    }

}
