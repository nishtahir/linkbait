package com.nishtahir.islackbot.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.islackbot.model.User
import com.nishtahir.islackbot.util.TimestampUtils

/**
 * Created by nish on 2/26/16.
 */
class UserService {

    Dao<User, Integer> userDao

    /**
     *
     * @param connectionSource Database connection source
     */
    UserService(ConnectionSource connectionSource) {
        userDao = DaoManager.createDao(connectionSource, User.class)
    }

    /**
     *
     * @param user
     * @return
     */
    def updateUser(User user) {
        userDao.update(user)
    }

    /**
     * This should be the primary way users are referenced.
     *
     * @param slackUserId
     * @return
     */
    def findUserBySlackUserId(String slackUserId) {
        def query = userDao.queryBuilder().where().eq("slackUserId", slackUserId).prepare();
        userDao.query(query)
    }

    /**
     *
     * @param username
     * @return
     */
    def findUserByName(String username) {
        def query = userDao.queryBuilder().where().eq("username", username).prepare();
        userDao.query(query)
    }

    /**
     * Adds a user to the database
     * @param user
     * @return
     */
    User createUser(User user) {
        return userDao.createIfNotExists(user)
    }

    /**
     * Returns a list of users.
     * @return
     */
    List<User> getUsers() {
        def query = userDao.queryBuilder()
                .where()
                .ge("upvotes", 1)
                .prepare()
        return userDao.query(query)
    }

    /**
     * Adds an upvote to a user.
     * @param user
     */
    void upvoteUser(User user) {
        user.upvotes++
        updateUser(user)
    }

    /**
     * Adds a downvote to a user. Evil >:D
     * @param user
     */
    void downvoteUser(User user){
        user.downvotes++
        updateUser(user)
    }
}
