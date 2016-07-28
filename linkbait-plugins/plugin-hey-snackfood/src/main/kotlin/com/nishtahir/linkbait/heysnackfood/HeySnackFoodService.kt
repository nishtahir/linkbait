package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

class HeySnackFoodService {

    val connectionSource: JdbcConnectionSource = InjektModule.scope.get()

    val userDao: Dao<User, Int> = DaoManager.createDao(connectionSource, User::class.java)

    init {
        TableUtils.createTableIfNotExists(connectionSource, User::class.java)
    }

    fun getLeaderboad(): List<User> {
        val query = userDao.queryBuilder().where()
                .ge(User::count.name, 1)
                .prepare()
        val leaderboard: List<User> = userDao.query(query)
        return leaderboard.sortedByDescending { it.count }.take(10).orEmpty()
    }

    fun findOrCreateUser(name: String): User {
        var user = userDao.queryBuilder().where().eq(User::name.name, name)
                .queryForFirst()

        if (user != null) {
            return user
        }

        user = User()
        user.name = name
        return user

    }

    fun createUser(name: String): User? {
        val user = User()
        user.name = name
        try {
            userDao.create(user)
            return user
        } catch (e: SQLException) {
            return null
        }
    }

    fun updateUser(user: User) {
        userDao.createOrUpdate(user)
    }
}