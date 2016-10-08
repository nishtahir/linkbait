package com.nishtahir.linkbait.config

import com.nishtahir.linkbait.commons.RandomNameGenerator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties(prefix = "bot")
open class BotConfiguration {

    var config: ArrayList<Config> = ArrayList()

    open class Config {
        var id: String = RandomNameGenerator().next()
        var key: String = ""
        var service: String = ""
    }
}