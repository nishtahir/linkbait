package com.nishtahir.linkbait

import com.google.common.util.concurrent.ServiceManager
import com.nishtahir.linkbait.config.BotConfiguration
import com.nishtahir.linkbait.config.ServerConfiguration
import com.nishtahir.linkbait.core.slack.SlackBot
import com.nishtahir.linkbait.extensions.e
import com.nishtahir.linkbait.extensions.i
import com.nishtahir.linkbait.extensions.logger
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
open class SlackClientComponent : DisposableBean {

    val SERVICE_NAME = "slack"

    val log = logger(SlackClientComponent::class.java)

    lateinit var serviceManager: ServiceManager

    @Autowired
    lateinit var botConfig: BotConfiguration

    @Autowired
    lateinit var serverConfig: ServerConfiguration

    @Bean
    open fun start(): Int {
        serviceManager = ServiceManager(getServices())
        serviceManager.startAsync()
        return 0
    }

    fun getServices(): List<SlackBot> = botConfig.config.filter {
        SERVICE_NAME == it.service.toLowerCase()
    }.map {
        SlackBot(serverConfig.repo, serverConfig.plugin, it.key, it.id)
    }

    override fun destroy() {
        try {
            serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
        } catch (timeout: TimeoutException) {
            log.e("Failed to stop services in time.", timeout)
        }
    }
}