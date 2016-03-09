package com.nishtahir.linkbait

import ch.qos.logback.classic.Logger
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.config.Configuration
import com.nishtahir.linkbait.controller.LinkController
import com.nishtahir.linkbait.controller.UserController
import com.nishtahir.linkbait.messages.Messages
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.request.HelpRequest
import com.nishtahir.linkbait.service.LinkService
import com.nishtahir.linkbait.service.UserService
import com.nishtahir.linkbait.util.PlayAttachmentCreator
import com.nishtahir.linkbait.util.SteamAttachmentCreator
import com.nishtahir.linkbait.util.TacoUtils
import com.nishtahir.linkbait.util.ValidationUtils
import com.ullink.slack.simpleslackapi.SlackMessageHandle
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.ReactionRemoved
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply
import groovy.json.JsonSlurper
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
     * Application configuration.
     * Typically configuration.yml
     */
    static Configuration configuration

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

        configuration.teams.each { group, apiToken ->

            SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(apiToken)
            slackSession.addMessagePostedListener([
                    onEvent: { SlackMessagePosted event, SlackSession session ->
                        if (event.sender == session.sessionPersona())
                            return

                        String message = event.messageContent
                        String senderUsername = event.sender.userName
                        String channelName = event.channel.name
                        String timestamp = event.timestamp
                        // Until the Slack library has a SlackChannel.isPrivate() method,
                        // we'll have to check the first char of the id. C = Public channel
                        boolean isPublic = event.getChannel().getId()[0] == 'C'

                        new HelpRequest(event, session)

                        final String url = ValidationUtils.getUrlFromSlackLink(message)
                        if (url != null) {

                            //Check if playstore URL
                            String playId = ValidationUtils.getPlaystoreId(url)
                            if (playId != null) {
                                session.sendMessage(event.channel, null, new PlayAttachmentCreator().getSlackAttachmentForUrl(url))
                            }

                            long steamId = ValidationUtils.getSteamId(url)
                            if (steamId != -1) {
                                session.sendMessage(event.channel, null, new SteamAttachmentCreator().getSlackAttachmentForUrl(url))
                            }
                            if (isPublic) {
                                session.addReactionToMessage(event.channel, timestamp, configuration.getUpvoteEmoji())
                                //Saving stuff usually takes a bit of time... might be nice delay
                                User user = userService.createUser(new User(username: senderUsername, slackUserId: event.sender.id))
                                linkService.saveLink(timestamp, url, user, group, channelName)
                                session.addReactionToMessage(event.channel, timestamp, configuration.getDownvoteEmoji())
                            }
                        }
                        //Check for taco request In message
                        if (TacoUtils.isValidTacoRequest(message, session.sessionPersona().id)) {
                            String recipient = TacoUtils.parseTacoRequest(message, session.sessionPersona().id)

                            if (recipient == 'me' || recipient == null) {
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
                                    session.addReactionToMessage(event.getChannel(), ts, configuration.getUpvoteEmoji())
                                    session.addReactionToMessage(event.getChannel(), ts, configuration.getDownvoteEmoji())

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
                            if (configuration.getUpvoteEmoji().equals(event.emojiName)) {
                                request.upvotes++

                            } else if (configuration.getDownvoteEmoji().equals(event.emojiName)) {
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

            slackSession.addReactionRemovedListener([
                    onEvent: { ReactionRemoved event, SlackSession session ->

                        if (request != null && event.messageID == request.timestamp) {
                            if (configuration.getUpvoteEmoji().equals(event.emojiName)) {
                                request.upvotes--

                            } else if (configuration.getDownvoteEmoji().equals(event.emojiName)) {
                                request.downvotes--
                            }

                            if (request.upvotes - request.downvotes >= 3) {
                                session.sendMessageOverWebSocket(event.getChannel(), "${request.user}: :taco:", null)
                                request = null
                            }

                        } else {
                            revokeUpvoteOrDownvoteFromLink(event.messageID, event.emojiName)
                        }
                    }
            ] as ReactionRemovedListener)

            slackSession.addMessageUpdatedListener([onEvent: { event, session ->

            }] as SlackMessageUpdatedListener)

            try {
                slackSession.connect();
            } catch (IOException e) {
                logger.error(e.printStackTrace())
            }
        }

    }

    /**
     * apply an upvote/downvote to a link
     * @param timestamp used as id
     * @param emoji up/downvote
     */
    void upOrDownvoteLink(String timestamp, String emoji) {
        if (configuration.getUpvoteEmoji().equals(emoji)) {

            Link link = linkService.upvoteLink(timestamp)
            userService.upvoteUser(link.publisher)

        } else if (configuration.getDownvoteEmoji().equals(emoji)) {

            Link link = linkService.downvoteLink(timestamp)
            userService.downvoteUser(link.publisher)

        }
    }

    /**
     * Removes an upvote or downvote previously applied
     * @param timestamp used as id
     * @param emoji up/downvote
     */
    void revokeUpvoteOrDownvoteFromLink(String timestamp, String emoji) {
        if (configuration.getUpvoteEmoji().equals(emoji)) {

            Link link = linkService.revokeUpvoteFromLink(timestamp)
            userService.revokeDownvoteFromUser(link.publisher)

        } else if (configuration.getDownvoteEmoji().equals(emoji)) {

            Link link = linkService.revokeDownvoteFromLink(timestamp)
            userService.revokeDownvoteFromUser(link.publisher)

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
     * Hard to guess what this does...
     */
    void initDatabase() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource(configuration.connection.url)
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
            usage: '[-c] configuration.yml'
            p longOpt: 'port', 'port to listen on', args: 1
            c longOpt: 'configuration', 'configuration file', args: 1, required: true
        }
        def options = cli.parse(args)
        options || exitWithMessage('Failed to parse arguments.')

        //Configuration
        File configFile = new File(options.c as String);
        configFile.exists() || exitWithMessage("File not found : ${configFile}")
        configuration = initConfig(configFile)

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