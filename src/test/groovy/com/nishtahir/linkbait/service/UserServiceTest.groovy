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

        then: "the update should persist"
        User databaseUser = userService.findUserBySlackUserId("1234")
        localUser == databaseUser
    }

    def "FindUserBySlackUserId_WithExistingUser_ReturnsUser"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is queried by his id"
        def foundDatabaseUser = userService.findUserBySlackUserId("1234")

        then: "the database user should exist and be the same as local"
        localUser == foundDatabaseUser
    }

    def "FindUserBySlackUserId_WithNonExistingUser_ReturnsEmptyList"() {
        when: "a non existing user is queried by his id"
        def foundDatabaseUser = userService.findUserBySlackUserId("1234")

        then: "he should not exist"
        foundDatabaseUser == null
    }


    def "FindUserByName_WithExistingUser_ReturnsUser"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is queried by his name"
        def foundDatabaseUser = userService.findUserByName("nish")

        then: "the database user should exist and be the same as local"
        localUser == foundDatabaseUser
    }

    def "FindUserByName_WithMissingUser_ReturnsEmptyList"() {
        when: "a non existing user is queried by his name"
        def foundDatabaseUser = userService.findUserByName("nish")

        then: "he should not exist"
        foundDatabaseUser == null
    }

    def "CreateUser_WithNewUser_CreatesUser"() {
        given: "a local user"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)

        when: "he is created in the database"
        userService.createUser(localUser)

        then: "he should exist in the database and be the same as local"
        def foundDatabaseUser = userService.findUserBySlackUserId("1234")
        localUser == foundDatabaseUser
    }

    def "UpvoteUser_WithExistingUser_UpvotesUser"() {
        given: "an already existing user in the database"
        final long expectedUpvotes = 11
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is upvoted"
        userService.upvoteUser(localUser)

        then: "the database user should have the expected upvotes"
        User databaseUser = userService.findUserBySlackUserId("1234")
        expectedUpvotes == databaseUser.upvotes
    }

    def "RevokeUpvoteFromUser_WithExistingUser_RevokesUpvoteFromUser"() {
        given: "an already existing user in the database"
        final long expectedUpvotes = 9
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "his upvote is revoked"
        userService.revokeUpvoteFromUser(localUser)

        then: "he should have the expected upvotes"
        User databaseUser = userService.findUserBySlackUserId("1234")
        expectedUpvotes == databaseUser.upvotes
    }

    def "DownvoteUser_WithExistingUser_DownvotesUser"() {
        given: "an already existing user in the database"
        final long expectedDownvotes = 6
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is downvoted"
        userService.downvoteUser(localUser)

        then: "he should have the expected downvotes"
        User databaseUser = userService.findUserBySlackUserId("1234")
        expectedDownvotes == databaseUser.downvotes
    }

    def "RevokeDownvoteFromUser_WithExistingUser_RevokesDowvoteFromUser"() {
        given: "an already existing user in the database"
        final long expectedDownvotes = 4
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "his downvote is revoked"
        userService.revokeDownvoteFromUser(localUser)

        then: "he should have the expected downvotes"
        User databaseUser = userService.findUserBySlackUserId("1234")
        expectedDownvotes == databaseUser.downvotes
    }
}
