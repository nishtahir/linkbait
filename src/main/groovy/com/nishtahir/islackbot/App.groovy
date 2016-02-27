package com.nishtahir.islackbot

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.islackbot.config.Configuration
import com.nishtahir.islackbot.controller.LinkController
import com.nishtahir.islackbot.controller.UserController
import com.nishtahir.islackbot.messages.Messages
import com.nishtahir.islackbot.model.Link
import com.nishtahir.islackbot.model.User
import com.nishtahir.islackbot.service.LinkService
import com.nishtahir.islackbot.service.UserService
import com.nishtahir.islackbot.util.PlayStoreUtils
import com.nishtahir.islackbot.util.TacoUtils
import com.nishtahir.islackbot.util.ValidationUtils
import com.ullink.slack.simpleslackapi.SlackMessageHandle
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import spark.Spark

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class App {

    /**
     * This is not a logger.
     */
    static final Logger logger = LoggerFactory.getLogger(App.class)

    /**
     * Highly coveted symbol of fake internet points.
     */
    static String EMOJI_ARROW_UP = 'upvote'

    /**
     * Beginning of all things depression related.
     */
    static String EMOJI_ARROW_DOWN = 'downvote'

    /**
     * Application configuration.
     * Typically config.yml
     */
    static Configuration config

    /**
     *
     */
    LinkService linkService

    /**
     *
     */
    UserService userService

    /**
     *
     */
    ConnectionSource connectionSource

    App() {
        initDatabase();
        initSlackBot()
        initApi()
    }

    static void main(String[] args) {
        parseOptions(args)
        new App()
    }

    TacoRequest request

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

                        final String url = ValidationUtils.getUrlFromSlackLink(message)
                        if (url != null) {

                            //Check if playstore URL
                            String playId = ValidationUtils.getPlaystoreId(url)
                            if (playId != null) {
                                session.sendMessage(event.channel, null, PlayStoreUtils.getPlayStoreDetailsAsSlackAttachment(url))
                            }

                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_UP)
                            //Saving stuff usually takes a bit of time... might be nice delay
                            User user = userService.createUser(new User(username: senderUsername, slackUserId: event.sender.id))
                            linkService.saveLink(timestamp, url, user, group, channelName)
                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_DOWN)

                        }
                        //Check for taco request In message

                        if (TacoUtils.isValidTacoRequest(message, session.sessionPersona().id)) {
                            String recipient = TacoUtils.parseTacoRequest(message, session.sessionPersona().id)

                            if(recipient == 'me' || recipient == null){
                                recipient = "<@${event.sender.id}>"
                            }

                            int chanceOfTaco = ThreadLocalRandom.current().nextInt(10)

                            if (chanceOfTaco < 2) {
                                session.sendMessageOverWebSocket(event.getChannel(), "$recipient :taco:", null)
                            } else {

                                if (request != null && request.isValid) {
                                    session.sendMessage(event.getChannel(), "Hang on. ${request.user} is already asking for a taco.", null)

                                } else {

                                    SlackMessageHandle<SlackMessageReply> handle = session.sendMessage(event.getChannel(),
                                            Messages.getRandomTacoMessage("$recipient"), null)
                                    handle.waitForReply(1000, TimeUnit.MILLISECONDS)
                                    //For some reason, they type returned GenericSlackReplyImpl throws a missing method exception
                                    //The only way i could get it to work is to manually slurp the json
                                    def result = new JsonSlurper().parseText(((GenericSlackReply) handle.getReply()).getPlainAnswer().toString())
                                    String ts = result['ts'].toString()
                                    session.addReactionToMessage(event.getChannel(), ts, EMOJI_ARROW_UP)
                                    session.addReactionToMessage(event.getChannel(), ts, EMOJI_ARROW_DOWN)

                                    request = new TacoRequest(
                                            timestamp: ts,
                                            user: "$recipient",
                                            upvotes: 0,
                                            downvotes: 0)
                                }
                            }
                        }
                    }
            ] as SlackMessagePostedListener);

            slackSession.addReactionAddedListener([
                    onEvent: { ReactionAdded event, SlackSession session ->

                        if (request != null && event.messageID == request.timestamp) {
                            if (EMOJI_ARROW_UP.equals(event.emojiName)) {
                                request.upvotes++

                            } else if (EMOJI_ARROW_DOWN.equals(event.emojiName)) {
                                request.downvotes++
                            }

                            if (request.upvotes - request.downvotes >= 3) {
                                session.sendMessageOverWebSocket(event.getChannel(), "${request.user}: :taco:", null)
                                request = null
                            }

                        } else {
                            upOrDownvoteLink(event.messageID, event.emojiName)
                        }
                    }
            ] as ReactionAddedListener)

            slackSession.addMessageUpdatedListener([onEvent: { event, session ->

            }] as SlackMessageUpdatedListener)

            try {
                slackSession.connect();
            } catch (IOException e) {
                logger.error(e.printStackTrace())
            }
        }

    }

    void upOrDownvoteLink(String timestamp, String emoji) {
        Link link
        if (EMOJI_ARROW_UP.equals(emoji)) {
            link = linkService.upvoteLink(timestamp)
            userService.upvoteUser(link.publisher)
        } else if (EMOJI_ARROW_DOWN.equals(emoji)) {
            link = linkService.downvoteLink(timestamp)
            userService.downvoteUser(link.publisher)
        }
    }

    /**
     * Init API routes and services.
     */
    void initApi() {
        userService = new UserService(connectionSource)
        linkService = new LinkService(connectionSource)
        new LinkController(linkService).init()
        new UserController(userService).init()
    }

    /**
     *
     */
    void initDatabase() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource(config.connection.url)
        TableUtils.createTableIfNotExists(connectionSource, Link.class)
        TableUtils.createTableIfNotExists(connectionSource, User.class)
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