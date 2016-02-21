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

    static def cli

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

    static main(args) {
        parseOptions(args)
        new App()

    }

    /**
     *
     */
    void initSlackBot() {

        config.teams.each { k, v ->
            SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(v)
            slackSession.addMessagePostedListener([
                    onEvent: { SlackMessagePosted event, SlackSession session ->
                        String message = event.messageContent
                        println("Username: ${session.sessionPersona().userName}, ID: ${session.sessionPersona().id}, RealName: ${session.sessionPersona().realName}");

                        println("Message: ${event.messageContent}, by ${event.sender.userName}")
                        String url = message.find(ValidationUtils.URL_PATTERN)
                        if (url) {
                            session.addReactionToMessage(event.getChannel(), event.timestamp, "arrow_up")
                            session.addReactionToMessage(event.getChannel(), event.timestamp, "arrow_down")

                            linkService.saveLink(event.timestamp, url, event.sender.userName)
                        }

                        if(ValidationUtils.isValidTacoRequest(event.messageContent, session.sessionPersona().id)){
                            session.sendMessageOverWebSocket(event.getChannel(), "<@${event.sender.id}> :taco:", null)
                        }
                    }
            ] as SlackMessagePostedListener);

            slackSession.addReactionAddedListener([
                    onEvent: { ReactionAdded event, SlackSession session ->
                        println("${event.emojiName}, ${event.getMessageID()}")
                        Link link = linkService.findLink(event.messageID)

                        if("arrow_up".equals(event.emojiName)){
                            link.upvotes++
                        } else if("arrow_down".equals(event.emojiName)){
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

    def initApi() {
        linkService = new LinkService(connectionSource)
        new LinkController(linkService).init()
    }

    def initDatabase() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource(config.connection.url)
        TableUtils.createTableIfNotExists(connectionSource, Link.class)


    }

    static def parseOptions(args) {
        cli = new CliBuilder()

        cli.with {
            usage: '[-c] config.yml'
            p longOpt: 'port', 'port to listen on', args: 1
            c longOpt: 'config', 'configuration file', args: 1, required: true
        }
        def options = cli.parse(args)
        options || System.exit(1)

        File file = new File(options.c as String);
        file.exists() || exitWithMessage("File not found : ${file}")
        config = new Yaml().loadAs(file.newInputStream(), Configuration.class)

        options.p && Spark.port(options.p as Integer)

    }

    static void exitWithMessage(message) {
        System.err.println(message);
        System.exit(1)
    }
}