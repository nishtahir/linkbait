package com.nishtahir.linkbait

import com.nishtahir.linkbait.extensions.logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LinkbaitServerApplication {

    val log = logger(LinkbaitServerApplication::class.java)

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(LinkbaitServerApplication::class.java, *args);
        }
    }
}