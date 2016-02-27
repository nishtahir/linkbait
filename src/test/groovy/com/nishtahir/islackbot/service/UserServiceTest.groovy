package com.nishtahir.islackbot.service

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.islackbot.model.Link
import com.nishtahir.islackbot.model.User
import spock.lang.Specification

/**
 * Created by nish on 2/26/16.
 */
class UserServiceTest extends Specification {

    UserService userService
    ConnectionSource connectionSource


    void setup() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource('jdbc:sqlite:linkbait-test.sqlite')
        TableUtils.createTableIfNotExists(connectionSource, User.class)

        userService = new UserService(connectionSource)
    }

    void cleanup() {
        connectionSource.close()
    }

    def "UpdateUser"() {

    }

    def "FindUserBySlackUserId"() {

    }

    def "CreateUser"() {

    }
}
