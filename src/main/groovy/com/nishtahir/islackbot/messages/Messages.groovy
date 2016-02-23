package com.nishtahir.islackbot.messages

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by nish on 2/23/16.
 */
class Messages {

    static final String USERNAME_PLACEHOLDER = "{{user}}"

    static final String[] tacoMessages = ["Does {{user}} deserve a taco?",
                         "Shall i bestow a taco onto {{user}}?",
                         "Time for a vote. {{user}} gets a taco. Yay or nay?",
                         "{{user}}, do you really think you should get a Taco?",
                         "Should {{user}} get a taco?",
                         "It is within my power to do so {{user}}, but the others shall decide your fate.",
                         "Shall I give {{user}} a taco?",
                         "Sounds like {{user}} is hungry. Upvotes = taco."]

    public static String getRandomTacoMessage(){
        return tacoMessages[ThreadLocalRandom.current().nextInt(tacoMessages.length)]
    }

    public static String getRandomTacoMessage(String username){
        getRandomTacoMessage().replace(USERNAME_PLACEHOLDER, username)
    }
}