package com.nishtahir.linkbait.config

import groovy.transform.Canonical

/**
 * Object representation of yml configuration file
 */
@Canonical
class Configuration {
    /**
     * Highly coveted symbol of fake internet points.
     */
    static final String EMOJI_UPVOTE = 'upvote'

    /**
     * Beginning of all things depression related.
     */
    static final String EMOJI_DOWNVOTE = 'downvote'

    /**
     * relevant yml
     *
     * version: {version number}*/
    String version

    /**
     * relevant yml
     *
     * emoji:
     *  {connection fields}
     */
    Map<String, String> emoji;

    /**
     * relevant yml
     *
     * connection:
     *  {connection fields}
     */
    Connection connection

    /**
     * relevant yml
     *
     * teams:
     *  {team name} : {api token}
     */Map< String, String> teams

    /**
     * upvote emoji property from config
     * @return
     */
    String getUpvoteEmoji(){
        return emoji?.get(EMOJI_UPVOTE)
    }

    /**
     * downvote emoji property from config
     * @return
     */
    String getDownvoteEmoji(){
        return emoji?.get(EMOJI_DOWNVOTE)
    }
}
