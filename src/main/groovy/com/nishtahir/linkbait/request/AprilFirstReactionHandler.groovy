package com.nishtahir.linkbait.request

import com.nishtahir.linkbait.exception.RequestParseException
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted

import java.util.concurrent.ThreadLocalRandom
/**
 * I'll leave it to you to guess what this is for
 */
@Singleton
class AprilFirstReactionHandler implements RequestHandler<Void, SlackMessagePosted> {

    static String[] emojis = [
            "1up", "2pac", "aliensguy", "all_the_things", "amazon", "america", "android", "angry_trump", "appleinc",
            "applepie", "asana", "aussieparrot", "aw_yeah", "back_away", "bacon", "bananadance", "bananas", "basecamp",
            "batman", "bb8", "beachball", "bernie_sanders", "bfalarm", "bfbored", "bfbow", "bfcomeon", "bfcongrats",
            "bfdetermine", "bfexcite", "bffrustrate", "bfhaha", "bfhide", "bfkiss", "bflove", "bfno", "bfohh", "bfquestion",
            "bfrun", "bfsob", "bfsorry", "bfstuck", "bftired", "bmo", "bobafett", "bobba_fett", "booom", "boredparrot",
            "bud_light", "budweiser", "bulbasaur", "burger", "bye_boo", "c3po", "candiedapple", "carlton", "cereal",
            "chandler_dance", "charizard", "charmander", "cheeseburger", "chewbacca", "chewie", "chocobo", "chrome",
            "clapping", "classy_pbj_time", "coin", "corona", "creamsicle", "creeper", "cremebrulee", "croissant", "cupcake",
            "dadcoffee", "dance", "dance_awkward", "darth_vader", "deadpool", "death_star", "deathstar", "deathstar2",
            "dickbutt", "ditto", "doge", "doge2", "domo", "donut", "doritos", "downvote",
            "droid", "android", "drool", "dropbox", "droplr", "easy_button", "edamame", "empire", "evil", "excited",
            "eyeroll", "facebook", "facepalm", "fingers_crossed", "finn", "fireball", "firefox", "flames", "flan", "flash",
            "fry", "fudge", "gandalf", "giggity", "github", "glowstick", "gmail", "good_job", "goofball", "google", "goomba",
            "gothparrot", "grumpy_cat", "grumpycat", "guinness", "gummyworms", "hadouken", "ham", "hammer_time", "handshake",
            "happening", "heres_johnny", "hodor", "homerdisppear", "hypnotoad", "illustrator", "indesign", "instagram",
            "internet_explorer", "ivysaur", "jake_wink", "jarjarbinks", "java", "javascript", "juice", "kanye_west",
            "kappa", "kebab", "kerbal_space_program", "kirby", "knuckles", "landspeeder", "laugh", "left_shark", "leftshark",
            "liftoff", "lime", "log", "lol", "macandcheese", "mailchimp", "mario", "mario-block", "matrix", "megalizard",
            "megusta", "mew", "mewtwo", "middleparrot", "milk", "milleniumfalcon", "mindblown", "minion_wave", "mj", "n64",
            "narwhal", "nemo", "netflix", "nigiri", "no_way_oprah", "notbad", "notes_minion", "notkanye", "notorious_big",
            "nutella", "nyancat", "nyancat_big", "olaf", "oldtimeparrot", "omg-panda", "onigiri", "panda_dance", "panic_button",
            "parrot", "partyparrot", "partybanana", "partyparrot", "pbr", "peace", "peanut_butter_jelly_time", "peekaboo", "pepperdance",
            "philosoraptor", "photoshop", "php", "pikachu", "pinterest", "pizza_party", "please_minion", "pokeball",
            "poolparty", "poptart", "postgresql", "powerup", "prettyinstant", "product-hunt", "psyduck", "pumpgirl",
            "pumpgirlrevers", "python", "question_block", "r2d2", "rainbow_flag", "raspberry_pi", "rebel", "rick_ross",
            "rightparrot", "rock", "rocket_launcher", "ron_swanson", "ruby", "rwing", "sad", "safari", "sandwich", "scotch",
            "sheepy", "shia", "shufflefurtherparrot", "shuffleparrot", "skype", "slurpee", "smh", "smore", "snoop_dancing",
            "snorlax", "sonic", "sonic-dance", "sonic-wait", "soundcloud", "soup", "sparta", "spiderman", "spinner",
            "spotify", "squirtle", "sriracha", "starbucks", "starwars", "stormtrooper", "swanson", "tails", "take_my_money",
            "taylor",
            "taytay", "taylor", "terrycrews", "thankyou", "the_more_you_know", "thebox", "thumb_down", "thumb_up", "tie-fighter",
            "tie_fighter", "tiefighter", "tiramisu", "todoist", "trello", "trump", "trump_hair", "tumbleweed", "twitter",
            "ugh_minion", "unicorn", "upvote", "wat", "watch_out", "water", "waving", "whatever_minion", "whoa", "why_not_both",
            "whyuno", "winky", "wololo", "wtf", "x-wing", "xwing", "yay", "yo", "yoda", "yoshi", "yuno", "whyuno",
            "ywing", "zoidberg"
    ]

    @Override
    Void parse(String message, String sessionId) {
        return null
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        try {
            int likelyhoodOfReaction = ThreadLocalRandom.current().nextInt(10)
            if(likelyhoodOfReaction > 7){
                for(i in 1 .. ThreadLocalRandom.current().nextInt(3, 10)){
                    Timer timer = new Timer()
                    timer.runAfter(i * 1000) {
                        session.addReactionToMessage(event.channel, event.timestamp, emojis[ThreadLocalRandom.current().nextInt(emojis.length)])
                    }
                }

            }
        } catch (RequestParseException ignore) {
        }
        //Never interrupt regular tasks
        return false
    }
}