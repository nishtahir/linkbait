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

//    @Bean
//    open fun demo(repository: UserRepository): CommandLineRunner {
//        return CommandLineRunner { args ->
//            repository.save(User(email = "test@test.com", password = "test"));
//            for (user in repository.findAll()) {
//                log.i(user.email)
//            }
//        }
//    }
}