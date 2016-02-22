package com.nishtahir.islackbot

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.islackbot.config.Configuration
import com.nishtahir.islackbot.controller.LinkController
import com.nishtahir.islackbot.model.Link
import com.nishtahir.islackbot.service.LinkService
import com.nishtahir.islackbot.util.ValidationUtils
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener
import org.yaml.snakeyaml.Yaml
import spark.Spark

class App {

    /**
     *
     */
    static String EMOJI_ARROW_UP = 'arrow_up'

    /**
     *
     */
    static String EMOJI_ARROW_DOWN = 'arrow_down'

    /**
     * Application configuration.
     * Typically config.yml
     */
    static Configuration config

    LinkService linkService

    ConnectionSource connectionSource

    App() {
        initDatabase();
        initSlackBot()
        initApi()
    }

    static main(String[] args) {
        parseOptions(args)
        new App()

    }

    /**
     *
     */
    void initSlackBot() {

        config.teams.each { group, apiToken ->

            SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(apiToken)
            slackSession.addMessagePostedListener([
                    onEvent: { SlackMessagePosted event, SlackSession session ->

                        String message = event.messageContent
                        String senderUsername = event.sender.userName
                        String channelName = event.channel.name
                        String timestamp = event.timestamp

                        //Check for link in message
                        String url = message.find(Patterns.WEB_URL)
                        if (url) {
                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_UP)
                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_DOWN)
                            linkService.saveLink(timestamp, url, senderUsername, group, channelName)
                        }

                        //Check for taco request In message
                        if (ValidationUtils.isValidTacoRequest(message, session.sessionPersona().id)) {
                            session.sendMessageOverWebSocket(event.getChannel(), "<@${event.sender.id}> :taco:", null)
                        }
                    }
            ] as SlackMessagePostedListener);

            slackSession.addReactionAddedListener([
                    onEvent: { ReactionAdded event, SlackSession session ->
                        String timestamp = event.messageID
                        Link link = linkService.findLink(timestamp)

                        if (EMOJI_ARROW_DOWN.equals(event.emojiName)) {
                            link.upvotes++
                        } else if (EMOJI_ARROW_DOWN.equals(event.emojiName)) {
                            link.downvotes++
                        }
                        linkService.updateLink(link)
                    }
            ] as ReactionAddedListener)

            slackSession.addMessageUpdatedListener([onEvent: { event, session ->

            }] as SlackMessageUpdatedListener)

            try {
                slackSession.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Init API routes and services.
     */
    void initApi() {
        linkService = new LinkService(connectionSource)
        new LinkController(linkService).init()
    }

    /**
     *
     */
    void initDatabase() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource(config.connection.url)
        TableUtils.createTableIfNotExists(connectionSource, Link.class)
    }

    /**
     * Parse comm
     * @param args
     */
    static void parseOptions(String[] args) {
        def cli = new CliBuilder()

        cli.with {
            usage: '[-c] config.yml'
            p longOpt: 'port', 'port to listen on', args: 1
            c longOpt: 'config', 'configuration file', args: 1, required: true
        }
        def options = cli.parse(args)
        options || exitWithMessage('Failed to parse arguments.')

        //Configuration
        File configFile = new File(options.c as String);
        configFile.exists() || exitWithMessage("File not found : ${configFile}")
        config = initConfig(configFile)

        //WebService Listening port
        options.p && Spark.port(options.p as Integer)
    }

    /**
     * Load configuration file
     * @param configFile file to load
     * @return configuration
     */
    static Configuration initConfig(File configFile) {
        new Yaml().loadAs(configFile.newInputStream(), Configuration.class)
    }

    /**
     * Print error message and terminate application
     * @param message message to print
     */
    static void exitWithMessage(message) {
        System.err.println(message);
        System.exit(1)
    }
}