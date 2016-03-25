package com.nishtahir.linkbait

import ch.qos.logback.classic.Logger
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.config.Configuration
import com.nishtahir.linkbait.controller.LinkController
import com.nishtahir.linkbait.controller.UserController
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.request.HelpRequestHandler
import com.nishtahir.linkbait.request.HoundifyMessageRequestHandler
import com.nishtahir.linkbait.request.NReactionHandler
import com.nishtahir.linkbait.request.RedditAutoCompleteHandler
import com.nishtahir.linkbait.request.TacoRequestHandler
import com.nishtahir.linkbait.service.LinkService
import com.nishtahir.linkbait.service.UserService
import com.nishtahir.linkbait.util.ValidationUtils
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.ReactionRemoved
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import spark.Spark

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

    /**
     *
     */
    void initSlackBot() {

        configuration.teams.each { group, apiToken ->

            SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(apiToken)
            slackSession.addMessagePostedListener([
                    onEvent: { SlackMessagePosted event, SlackSession session ->
                        if (event.sender.id == session.sessionPersona().id)
                            return

                        String message = event.messageContent
                        String senderUsername = event.sender.userName
                        String channelName = event.channel.name
                        String timestamp = event.timestamp
                        // Until the Slack library has a SlackChannel.isPrivate() method,
                        // we'll have to check the first char of the id. C = Public channel
                        boolean isPublic = event.getChannel().getId()[0] == 'C'

                        boolean helpHandled = HelpRequestHandler.instance.handle(session, event)

                        NReactionHandler.instance.handle(session,event)

                        boolean redditHandled = RedditAutoCompleteHandler.instance.handle(session, event)

                        final String url = ValidationUtils.getUrlFromSlackLink(message)
                        boolean urlHandled = false;
                        if (url != null) {

                            //Check if playstore URL
                            String playId = ValidationUtils.getPlaystoreId(url)
                            if (playId != null) {
                                session.sendMessage(event.channel, null, new PlayAttachmentCreator().getSlackAttachmentForUrl(url))
                                urlHandled = true
                            }

                            long steamId = ValidationUtils.getSteamId(url)
                            if (steamId != -1) {
                                session.sendMessage(event.channel, null, new SteamAttachmentCreator().getSlackAttachmentForUrl(url))
                                urlHandled = true
                            }
                            if (isPublic) {
                                session.addReactionToMessage(event.channel, timestamp, configuration.getUpvoteEmoji())
                                //Saving stuff usually takes a bit of time... might be nice delay
                                User user = userService.createUser(new User(username: senderUsername, slackUserId: event.sender.id))
                                linkService.saveLink(timestamp, url, user, group, channelName)
                                session.addReactionToMessage(event.channel, timestamp, configuration.getDownvoteEmoji())
                            }
                        }
                        boolean tacoHandled = TacoRequestHandler.instance.handle(session, event)

                        // We fall through here at the end, if none of these events triggered, use houndify
                        if(!helpHandled && !redditHandled && !urlHandled && !tacoHandled) {
                            HoundifyMessageRequestHandler.instance.handle(session, event)
                        }
                    }
            ] as SlackMessagePostedListener);

            slackSession.addReactionAddedListener([
                    onEvent: { ReactionAdded event, SlackSession session ->
                        TacoRequestHandler.instance.handleVote(session, event)
                        upOrDownvoteLink(event.messageID, event.emojiName)
                    }
            ] as ReactionAddedListener)

            slackSession.addReactionRemovedListener([
                    onEvent: { ReactionRemoved event, SlackSession session ->
                        TacoRequestHandler.instance.handleVote(session, event)
                        revokeUpvoteOrDownvoteFromLink(event.messageID, event.emojiName)
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