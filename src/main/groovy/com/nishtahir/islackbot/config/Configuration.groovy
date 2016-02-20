package com.nishtahir.islackbot.config

import groovy.transform.Canonical

/**
 * Created by nish on 2/19/16.
 */
@Canonical
class Configuration {

    String version

    Connection connection

    Map< String, String> teams
}
