package com.nishtahir.linkbait.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
@ConfigurationProperties(prefix = "server")
open class ServerConfiguration {

    var name: String = UUID.randomUUID().toString()

    var port: Int = 8080

    var static: File = File("./data/static")

    var plugin: File = File("./data/plugin")

    var repo: File = File("./data/repo")

    var temp: File = File("./data/temp")

}