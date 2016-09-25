package com.nishtahir.linkbait.core.discord

import com.google.common.eventbus.EventBus
import com.nishtahir.linkbait.core.AbstractBot
import com.nishtahir.linkbait.plugin.MessageEvent
import com.nishtahir.linkbait.plugin.Messenger
import com.nishtahir.linkbait.plugin.model.Configuration
import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import org.jetbrains.annotations.NotNull
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.MessageReceivedEvent

/**
 * Created by maxke on 20.08.2016.
 * Allows linkbait to talk to the Discord API
 */
@Slf4j
class DiscordBot extends AbstractBot {

    /**
     * Our main entry to the world of Discord
     */
    private IDiscordClient client;

    /**
     *  Provided by Discord for access to the guild
     */
    private String apiToken

    /**
     * Cause groovy is still weird
     */
    private Messenger messenger

    /**
     *
     * @param configuration
     */
    DiscordBot(@NotNull Configuration configuration, String apiToken, String owner) {
        super(configuration)
        this.apiToken = apiToken
        this.owner = owner

        eventBus = new EventBus(owner)
        init(apiToken)
    }

    /**
     * RELEASE THE KRAKEN (bot)
     * @param apiToken for discord
     */
    private void init(String apiToken) {
        client = new ClientBuilder().withToken(apiToken).build()
        client.getDispatcher().registerListener(new IListener<MessageReceivedEvent>() {
            @Override
            void handle(MessageReceivedEvent event) {
                // No need to check if this was our message since that is a different event on Discord4J
                MessageEvent messageEvent = new MessageEvent(id: event.message.timestamp,
                        channel: event.message.channel.name,
                        sender: "${event.message.author.name}#${event.message.author.discriminator}",
                )

                if (event.message.mentions.size() > 0 && event.message.mentions[0].getID() == client.ourUser.getID()) {
                    messageEvent.directedAtBot = true
                    messageEvent.directMessage = event.message.channel.isPrivate()

                    messageEvent.message = escapeDiscordIds(event.message.content.replaceFirst("<@${client.ourUser.getID()}>\\s*", ""))
                } else {
                    messageEvent.directedAtBot = false
                    messageEvent.directMessage = event.message.channel.isPrivate()
                    messageEvent.message = escapeDiscordIds(event.message.content)
                }
                eventBus.post(messageEvent)
            }
        })
    }

    @Override
    protected void startUp() throws Exception {
        super.startUp()
        client.login()
    }

    @Override
    protected void shutDown() throws Exception {
        super.shutDown()
        client.logout()
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
    Messenger getMessenger() {
        if (messenger == null) {
            messenger = new DiscordMessenger(client: client)
        }
        return messenger
    }

    @Override
    void setMessenger(Messenger messenger) {
        this.messenger = messenger
    }

    @Override
    void getPluginState() {
        // Here be Nutella
    }

    String escapeDiscordIds(String message) {
        def matcher = (message =~ /.*<@(?<id>\d+)>.*/)
        if (matcher.matches()) {
            matcher[0].eachWithIndex { String match, int i ->
                if (i == 0) return
                message = message.replace("<@${match}>", "@${client.getUserByID(match as String).getName()}")
            }
        }
        return message
    }
}
