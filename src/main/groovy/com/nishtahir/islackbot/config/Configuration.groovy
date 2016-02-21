package com.nishtahir.islackbot.config

import groovy.transform.Canonical

/**
 * Object representation of yml config file
 */
@Canonical
class Configuration {

    /**
     * relevant yml
     *
     * version: {version number}
     */
    String version

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
     */
    Map< String, String> teams
}
