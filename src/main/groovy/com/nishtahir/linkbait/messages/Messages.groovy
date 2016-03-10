package com.nishtahir.linkbait.messages

import org.apache.commons.lang3.StringUtils

import java.util.concurrent.ThreadLocalRandom

class Messages {

    static final String[] TACO_MESSAGES = [
        "Does {{user}} deserve a taco?",
        "Shall i bestow a taco onto {{user}}?",
        "Time for a vote. {{user}} gets a taco. Yay or nay?",
        "{{user}}, do you really think you should get a Taco?",
        "Should {{user}} get a taco?",
        "It is within my power to do so {{user}}, but the others shall decide your fate.",
        "Shall I give {{user}} a taco?",
        "Sounds like {{user}} is hungry. Upvotes = taco."
    ]

    static final String[] TACO_DENIED_MESSAGES = [
        ":taco: Denied!",
        "{{user}}: Sorry, no taco for you!",
        "Sorry {{user}}, but your taco is in another Slack group.",
        "{{user}}, better luck next time",
        "{{user}}, I can't give you a taco but will a :nutella: do instead?",
        "The people have spoken! No taco shall be given today.",
        "{{user}}: Sorry buddy, best I can give you is :partyparrot:"
    ]
    static final String[] HELP = [
        "Hi, my name is {{bot}}!",
        "I'm an open source bot of AndroidChat, find me <https://gitlab.com/nishtahir/linkbait|here>.",
        "I allow people to vote on links, keep track of votes and can give out tacos!"
    ]

    public static String formatMessage(String message, String... args) {
        if(args.length % 2 != 0)
            throw new IllegalArgumentException("There needs to be an even amount of key and value arguments.")
        for(int i = 0; i < args.length; i += 2) {
            message = message.replaceAll(/\{\{\s?${args[i]}\s?\}\}/, args[i + 1]);
        }
        return message;
    }

    public static String[] formatMessages(String[] messages, String... args) {
        for(int i = 0; i < messages.length; i++) {
            messages[i] = formatMessage(messages[i], args);
        }
        return messages;
    }

    public static String getRandomTacoMessage() {
        return TACO_MESSAGES[ThreadLocalRandom.current().nextInt(TACO_MESSAGES.length)]
    }

    public static String getRandomTacoMessage(String username) {
        return formatMessage(getRandomTacoMessage(), "user", username);
    }

    public static String getHelpMessage(String bot) {
        String[] messages = formatMessages(HELP, "bot", bot);
        return StringUtils.join(messages, "\n");
    }

    static String getRandomTacoDeniedMessage() {
        return TACO_DENIED_MESSAGES[ThreadLocalRandom.current().nextInt(TACO_DENIED_MESSAGES.length)]
    }

    static String getRandomTacoDeniedMessage(String user){
        return formatMessage(getRandomTacoDeniedMessage(), "user", user);
    }
}