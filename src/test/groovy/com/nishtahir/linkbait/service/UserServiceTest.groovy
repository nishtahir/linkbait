package com.nishtahir.linkbait.service

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.model.User
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
        TableUtils.clearTable(connectionSource, User.class)
        connectionSource.close()
    }

    def "UpdateUser_WithExistingUser_UpdatesCorrectly"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is updated in the database"
        localUser.upvotes = 7
        localUser.downvotes = 10
        userService.updateUser(localUser)

        then: "he should be updated in the database as well"
        User databaseUser = userService.findUserByName("nish").first()
        localUser == databaseUser
    }

    def "FindUserBySlackUserId_WithExistingUser_ReturnsUser"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is queried by his id"
        List<User> foundDatabaseUsers = userService.findUserBySlackUserId("1234")

        then: "he should be same as local"
        foundDatabaseUsers.size() == 1
        localUser == foundDatabaseUsers.first()
    }

    def "FindUserBySlackUserId"() {

    }

    def "CreateUser"() {

    }
}
