package com.nishtahir.islackbot

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.islackbot.config.Configuration
import com.nishtahir.islackbot.controller.LinkController
import com.nishtahir.islackbot.messages.Messages
import com.nishtahir.islackbot.model.Link
import com.nishtahir.islackbot.service.LinkService
import com.nishtahir.islackbot.util.ValidationUtils
import com.ullink.slack.simpleslackapi.SlackAttachment
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
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.yaml.snakeyaml.Yaml
import spark.Spark

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

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

    static void main(String[] args) {
        parseOptions(args)
        new App()
    }

    Map<String, Long> currentlyRequestingTaco

    TacoRequest request

    /**
     *
     */
    void initSlackBot() {

        config.teams.each { group, apiToken ->

            SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(apiToken)
            slackSession.addMessagePostedListener([
                    onEvent: { SlackMessagePosted event, SlackSession session ->
                        println("-----------------------------------------------")
                        String message = event.messageContent
                        String senderUsername = event.sender.userName
                        String channelName = event.channel.name
                        String timestamp = event.timestamp

                        final String url = ValidationUtils.getUrlFromSlackLink(message)
                        if (url != null) {

                            //Check if playstore URL
                            String playId = ValidationUtils.getPlaystoreId(url)
                            if (playId != null) {

                                Map<String, String> playstoreDetails = getAppDetailsFromPlayStore(playId)
                                SlackAttachment attachment = new SlackAttachment()
                                attachment.fallback = playstoreDetails['title']
                                attachment.title = playstoreDetails['title']
                                attachment.titleLink = url
                                attachment.thumb_url = playstoreDetails['imageUrl']
                                attachment.color = '3F51B5' //Material Indigo 500
                                attachment.text = playstoreDetails['desc']
                                attachment.addField("Author", playstoreDetails['author'], true)
                                attachment.addField("Price", playstoreDetails['price'], true)
                                attachment.addField("Last updated", playstoreDetails['lastUpdated'], true)
                                attachment.addField("Downloads", playstoreDetails['dlCount'], true)
                                attachment.addField("All hail his Majesty", "@nish", false)

                                session.sendMessage(event.channel, null, attachment)
                            }

                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_UP)
                            //Saving stuff usually takes a bit of time... might be nice delay
                            linkService.saveLink(timestamp, url, senderUsername, group, channelName)
                            session.addReactionToMessage(event.channel, timestamp, EMOJI_ARROW_DOWN)

                        }
                        //Check for taco request In message
                        if (ValidationUtils.isValidTacoRequest(message, session.sessionPersona().id)) {
                            int chanceOfTaco = ThreadLocalRandom.current().nextInt(10)

                            if(chanceOfTaco < 2){
                                session.sendMessageOverWebSocket(event.getChannel(), "<@${event.sender.id}> :taco:", null)
                            } else {

                                if (request != null && request.isValid) {
                                    session.sendMessage(event.getChannel(), "Hang on. ${request.user} is already asking for a taco.", null)

                                } else {

                                    SlackMessageHandle<SlackMessageReply> handle = session.sendMessage(event.getChannel(), Messages.getRandomTacoMessage("<@${event.sender.id}>"), null)
                                    handle.waitForReply(1000, TimeUnit.MILLISECONDS)
                                    //For some reason, they type returned GenericSlackReplyImpl throws a missing method exception
                                    //The only way i could get it to work is to manually slurp the json
                                    def result = new JsonSlurper().parseText(((GenericSlackReply) handle.getReply()).getPlainAnswer().toString())
                                    String ts = result['ts'].toString()
                                    session.addReactionToMessage(event.getChannel(), ts, EMOJI_ARROW_UP)
                                    session.addReactionToMessage(event.getChannel(), ts, EMOJI_ARROW_DOWN)

                                    request = new TacoRequest(
                                            timestamp: ts,
                                            user: "<@${event.sender.id}>",
                                            upvotes: 0,
                                            downvotes: 0)
                                }
                            }
                        }
                    }
            ] as SlackMessagePostedListener);

            slackSession.addReactionAddedListener([
                    onEvent: { ReactionAdded event, SlackSession session ->

                        if(request != null && event.messageID == request.timestamp){
                            if (EMOJI_ARROW_UP.equals(event.emojiName)) {
                                request.upvotes++
                            } else if (EMOJI_ARROW_DOWN.equals(event.emojiName)) {
                                request.downvotes++
                            }

                            if(request.upvotes - request.downvotes >= 3){
                                session.sendMessageOverWebSocket(event.getChannel(), "${request.user}: :taco:", null)
                                request = null
                            }

                        } else {

                            String timestamp = event.messageID
                            Link link = linkService.findLink(timestamp)

                            if (link != null) {
                                if (EMOJI_ARROW_UP.equals(event.emojiName)) {
                                    link.upvotes++
                                } else if (EMOJI_ARROW_DOWN.equals(event.emojiName)) {
                                    link.downvotes++
                                }
                                linkService.updateLink(link);
                            }
                        }
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

    static final int MAX_DESC_LENGTH = 2000;

    /**
     * Parse playstore app content
     * @param playId
     * @return
     */
    static Map<String, String> getAppDetailsFromPlayStore(String playId) {
        String link = "https://play.google.com/store/apps/details?id=${playId}&hl=en&gl=us"
        String html = link.toURL().text
        Document doc = Jsoup.parse(html)

        def fields = [:]

        fields.put('title', doc.select('.document-title div').text().trim())
        fields.put('author', doc.select('div[itemprop=author] a span').text().trim())
        fields.put('desc', escapeHtml(doc.select('div[itemprop=description]').text().take(MAX_DESC_LENGTH)))
        fields.put('imageUrl', doc.select('.cover-image').attr('src').trim())
        fields.put('score', doc.select('.score').text().trim())
        fields.put('reviews', doc.select('.reviews-num').text().trim())
        fields.put('lastUpdated', doc.select('div[itemprop=datePublished]').text().trim())
        fields.put('dlCount', doc.select('div[itemprop=numDownloads]').text().trim())
        fields.put('version', doc.select('div[itemprop=softwareVersion]').text().trim())
        fields.put('osVersion', doc.select('div[itemprop=operatingSystems]').text().trim())
        fields.put('contentRating', doc.select('div[itemprop=contentRating]').text().trim())
        fields.put('price', doc.select('meta[itemprop=price]').attr('content').trim().replace("0", "Free"))

        return fields
    }

    /**
     * @param html stuff that needs to be formatted
     * @return String with the tags escaped
     */
    static String escapeHtml(CharSequence html) {
        return StringEscapeUtils.escapeHtml4(String.valueOf(html))
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