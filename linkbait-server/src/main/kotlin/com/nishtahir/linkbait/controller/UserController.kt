//package com.nishtahir.linkbait.model
//
//import com.j256.ormlite.dao.DaoManager
//import com.j256.ormlite.jdbc.JdbcConnectionSource
//import com.nishtahir.linkbait.extensions.dataToJson
//import com.nishtahir.linkbait.extensions.getEmailFromQuery
//import com.nishtahir.linkbait.extensions.getPasswordFromQuery
//import com.nishtahir.linkbait.extensions.getUsernameFromQuery
//import org.apache.commons.validator.routines.EmailValidator
//import org.mindrot.BCrypt
//import spark.Request
//import spark.Response
//import uy.kohesive.injekt.Injekt
//import uy.kohesive.injekt.api.get
//import java.sql.SQLException
//
//val connectionSource: JdbcConnectionSource = Injekt.get()
//val userDao = DaoManager.createDao(connectionSource, User::class.java)
//
///**
// * @param email
// * @param password
// * @return true if credentials are valid
// */
//fun authenticate(email: String, password: String): Boolean {
//    if (email.isBlank() || password.isBlank()) {
//        return false
//    }
//    val user = findUserByEmail(email)
//    if (user == null) {
//        return false
//    } else {
//        return authenticate(user, password)
//    }
//}
//
//fun authenticate(user: User, password: String): Boolean {
//
//    if (user.email.isBlank() || password.isBlank()) {
//        return false
//    }
//    return BCrypt.checkpw(password, user.password)
//}
//
///**
// * @param name
// * @param email
// * @param password
// * Creates a new user
// */
//fun createUser(name: String, email: String, password: String): User? {
//    val user = User(name, email, BCrypt.hashpw(password, BCrypt.gensalt()))
//    try {
//        userDao.create(user)
//        return user
//    } catch (e: SQLException) {
//        return null
//    }
//}
//
///**
// * @param email address to query
// * @return Matching #User
// */
//fun findUserByEmail(email: String): User? {
//    val query = userDao.queryBuilder()
//            .where()
//            .eq(User::email.name, email)
//            .prepare()
//    return userDao.queryForFirst(query)
//}
//
///**
// * @param id to query
// * @return Matching #User
// */
//fun findUserById(id: Long): User? {
//    val query = userDao.queryBuilder()
//            .where()
//            .eq(User::id.name, id)
//            .prepare()
//    return userDao.queryForFirst(query)
//}
//
///**
// *
// */
//fun findUserByName(name: String): User? {
//    val query = userDao.queryBuilder()
//            .where()
//            .eq(User::name.name, name)
//            .prepare()
//    return userDao.queryForFirst(query)
//}
//
///**
// *
// */
//val handleCreateUser = fun(request: Request, response: Response): String {
//    val userName = getUsernameFromQuery(request)
//    val email = getEmailFromQuery(request)
//    val password = getPasswordFromQuery(request)
//
//    if (!isValidEmail(email)) {
//        response.status(BAD_REQUEST)
//        return dataToJson(ServerError(INVALID_EMAIL, "Invalid Email", "The email provided could not be validated"))
//    }
//
//    if (!isValidUsername(userName)) {
//        response.status(BAD_REQUEST)
//        return dataToJson(ServerError(INVALID_USERNAME, "Invalid username", "The username provided could not be validated"))
//    }
//
//    if (password.isNullOrBlank()) {
//        response.status(BAD_REQUEST)
//        return dataToJson(ServerError(INVALID_PASSWORD, "Invalid password", "The password provided is not valid"))
//    }
//
//    val user = createUser(userName, email, password)
//    if (user != null) {
//        return dataToJson(user)
//    } else {
//        response.status(INTERNAL_SERVER_ERROR)
//        return dataToJson(ServerError(OTHER_ERROR, "Error occurred when creating user",
//                "An error occurred when trying to create the user. That's all I know."))
//    }
//}
//
//val handleLogin = fun(request: Request, response: Response): String {
//    findUserByEmail(getEmailFromQuery(request))?.let { user ->
//        if (authenticate(user, getPasswordFromQuery(request))) {
//            response.redirect("/${user.name}/")
//        }
//
//    }
//    return "error"
//}
//
//val handleSearch = fun(request: Request, response: Response): String {
//    findUserByName(getUsernameFromQuery(request))?.let {
//        response.status(OK)
//        return dataToJson("{'ok':'ok'}")
//    }
//
//    response.status(BAD_REQUEST)
//    return dataToJson(ServerError(USERNAME_IN_USE, "username is in use", "need different username"))
//}
//
///**
// *
// */
//val handleUser = fun(request: Request, response: Response): String {
//    val email = getEmailFromQuery(request)
//    if (isValidEmail(email)) {
//        val user = findUserByEmail(email)
//        return user?.let { dataToJson(user) }.orEmpty()
//    }
//    return dataToJson(ServerError(0, "User not found", "The user could not be found."))
//}
//
//val USERNAME_PATTERN = """^[a-zA-Z0-9_-]{3,20}$""".toRegex();
//
///**
// *  @param username Username to validate
// *  @return true if valid
// */
//fun isValidUsername(username: String): Boolean {
//    return USERNAME_PATTERN.matches(username)
//}
//
///**
// * @param email Email address
// * @return true if valid
// */
//fun isValidEmail(email: String): Boolean {
//    return EmailValidator.getInstance().isValid(email)
//}
//
//

