package com.nishtahir.linkbait.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties(prefix = "bot")
open class BotConfiguration {

    var config: ArrayList<Config> = ArrayList()

    open class Config() {
        var id: String = UUID.randomUUID().toString()
        var key: String = ""
        var service: String = ""
    }
}