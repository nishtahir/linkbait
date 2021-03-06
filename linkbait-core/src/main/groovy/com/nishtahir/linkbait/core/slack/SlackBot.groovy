package com.nishtahir.linkbait.core.slack

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.AbstractBot
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.ReactionEvent
import com.nishtahir.linkbait.plugin.model.Configuration
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.ReactionAdded
import com.ullink.slack.simpleslackapi.events.ReactionRemoved
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import org.jetbrains.annotations.NotNull

/**
 * Supports slack using the slack api.
 */
@Canonical
@ToString
@Slf4j
class SlackBot extends AbstractBot {

    /**
     *  Where all the communication magic happens.
     */
    private SlackSession session

    /**
     *  Provided by Slack for access to the team
     */
    private String apiToken

    /**
     *
     */
    private Messenger messenger

    /**
     *
     * @param pluginRepository
     * @param pluginDirectory
     * @param apiToken
     * @param id
     */
    SlackBot(@NotNull File pluginRepository, @NotNull File pluginDirectory,
             @NotNull String apiToken, @NotNull String id) {
        super(pluginRepository, pluginDirectory)
        this.apiToken = apiToken
        this.id = id

        eventBus = new EventBus(id)
        init(apiToken)
    }

    /**
     *
     * @param configuration
     * @param apiToken
     * @param owner
     */
    @Deprecated
    SlackBot(Configuration configuration, String apiToken, String owner) {
        this(configuration.pluginRepository, configuration.pluginDirectory, apiToken, owner)
    }

    /**
     * Initialize even and start session
     * @param apiToken for slack
     * @param owner identifies the eventbus and other properties
     */
    private void init(@NotNull String apiToken) {
        session = SlackSessionFactory.createWebSocketSlackSession(apiToken);
        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            void onEvent(SlackMessagePosted event, SlackSession session) {
                if (event.sender.id != session.sessionPersona().id) {
                    MessageEvent messageEvent = new MessageEvent(id: event.timestamp,
                            channel: event.channel.name,
                            sender: event.sender.userName,
                    )
                    def matcher = (event.messageContent =~ /^(?i)(<@${session.sessionPersona().id}>:?)\s+(?<text>(.*))/)
                    if (matcher.find()) {
                        messageEvent.directedAtBot = true
                        messageEvent.directMessage = event.channel.direct

                        messageEvent.message = escapeSlackIds(matcher.group('text'))
                    } else {
                        messageEvent.directedAtBot = false
                        messageEvent.directMessage = event.channel.direct
                        messageEvent.message = escapeSlackIds(event.messageContent)
                    }
                    eventBus.post(messageEvent)
                }
            }
        })
        session.addReactionAddedListener(new ReactionAddedListener() {
            @Override
            void onEvent(ReactionAdded event, SlackSession session) {
                ReactionEvent reactionEvent = new ReactionEvent(id: event.messageID,
                        channel: event.channel.name,
                        sender: event.user.userName,
                        reaction: event.emojiName,
                        added: true)
                if (event.getUser().id != session.sessionPersona().id) {
                    eventBus.post(reactionEvent)
                }
            }
        })
        session.addReactionRemovedListener(new ReactionRemovedListener() {
            @Override
            void onEvent(ReactionRemoved event, SlackSession session) {
                ReactionEvent reactionEvent = new ReactionEvent(id: event.messageID,
                        channel: event.channel.name,
                        sender: event.user.userName,
                        reaction: event.emojiName,
                        added: false)
                if (event.getUser().id != session.sessionPersona().id) {
                    eventBus.post(reactionEvent)
                }
            }
        })
    }

    @Override
    protected void startUp() throws Exception {
        super.startUp()
        log.info("Starting: $id")
        session.connect();
    }

    @Override
    protected void run() throws Exception {
        super.run();
        while (isRunning()) {
            //Don't know if this is a good idea, but
            //it seemed wrong to let the process run in an
            //unmanaged infinite loop
            Thread.sleep(500);
        }
    }

    @Override
    protected void shutDown() throws Exception {
        super.shutDown()
        log.info("Stopping: $id")
        session.disconnect();
    }

    @Override
    @NotNull
    Messenger getMessenger() {
        if (messenger == null) {
            messenger = new SlackMessenger(session: session)
        }
        return messenger
    }

    @Override
    void setMessenger(@NotNull Messenger messenger) {
        this.messenger = messenger
    }

    @Override
    void getPluginState() {

    }

    @NotNull
    String escapeSlackIds(@NotNull String message) {
        def matcher = (message =~ /.*<@(?<id>\w+)>.*/)
        if (matcher.matches()) {
            matcher[0].eachWithIndex { String match, int i ->
                if (i == 0) return
                message = message.replace("<@${match}>", "@${session.findUserById(match as String).userName}")
            }
        }
        return message
    }
}
