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
        User databaseUser = userService.findUserBySlackUserId("1234").first()
        localUser == databaseUser
    }

    def "FindUserBySlackUserId_WithExistingUser_ReturnsUser"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is queried by his id"
        List<User> foundDatabaseUsers = userService.findUserBySlackUserId("1234")

        then: "the database user should exist and be the same as local"
        foundDatabaseUsers.size() == 1
        localUser == foundDatabaseUsers.first()
    }

    def "FindUserBySlackUserId_WithNonExistingUser_ReturnsEmptyList"() {
        when: "a non existing user is queried by his id"
        List<User> foundDatabaseUsers = userService.findUserBySlackUserId("1234")

        then: "he should not exist"
        foundDatabaseUsers.size() == 0
    }


    def "FindUserByName_WithExistingUser_ReturnsUser"() {
        given: "an already existing user in the database"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is queried by his name"
        List<User> foundDatabaseUsers = userService.findUserByName("nish")

        then: "the database user should exist and be the same as local"
        foundDatabaseUsers.size() == 1
        localUser == foundDatabaseUsers.first()
    }

    def "FindUserByName_WithMissingUser_ReturnsEmptyList"() {
        when: "a non existing user is queried by his name"
        List<User> foundDatabaseUsers = userService.findUserByName("nish")

        then: "he should not exist"
        foundDatabaseUsers.size() == 0
    }

    def "CreateUser_WithNewUser_CreatesUser"() {
        given: "a local user"
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)

        when: "he is created in the database"
        userService.createUser(localUser)

        then: "he should exist in the database and be same as local"
        List<User> foundDatabaseUsers = userService.findUserBySlackUserId("1234")
        foundDatabaseUsers.size() == 1
        localUser == foundDatabaseUsers.first()
    }

    def "UpvoteUser_WithExistingUser_UpvotesUser"() {
        given: "an already existing user in the database"
        final long expectedUpvotes = 11
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is upvoted"
        userService.upvoteUser(localUser)

        then: "he should have the same upvotes as local"
        User databaseUser = userService.findUserBySlackUserId("1234").first()
        expectedUpvotes == databaseUser.upvotes
    }

    def "RevokeUpvoteFromUser_WithExistingUser_RevokesUpvotesUser"() {
        given: "an already existing user in the database"
        final long expectedUpvotes = 9
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is upvoted"
        userService.revokeUpvoteFromUser(localUser)

        then: "he should have the same upvotes as local"
        User databaseUser = userService.findUserBySlackUserId("1234").first()
        expectedUpvotes == databaseUser.upvotes
    }

    def "DownvoteUser_WithExistingUser_DownvotesUser"() {
        given: "an already existing user in the database"
        final long expectedDownvotes = 6
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "he is downvoted"
        userService.downvoteUser(localUser)

        then: "he should have the same downvotes as local"
        User databaseUser = userService.findUserBySlackUserId("1234").first()
        expectedDownvotes == databaseUser.downvotes
    }

    def "RevokeDownvoteFromUser_WithExistingUser_RevokesDowvoteFromUser"() {
        given: "an already existing user in the database"
        final long expectedDownvotes = 4
        User localUser = new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5)
        userService.createUser(localUser)

        when: "his downvote is revoked"
        userService.revokeDownvoteFromUser(localUser)

        then: "he should have the same downvotes as local"
        User databaseUser = userService.findUserBySlackUserId("1234").first()
        expectedDownvotes == databaseUser.downvotes
    }
}
