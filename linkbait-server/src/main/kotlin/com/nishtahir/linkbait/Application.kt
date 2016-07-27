package com.nishtahir.linkbait

import com.j256.ormlite.db.DerbyEmbeddedDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.extensions.*
import com.nishtahir.linkbait.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Spark
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addLoggerFactory
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.get
import uy.kohesive.injekt.injectLogger
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.SQLException
import java.util.*

/**
 *  Entry point for app.
 *  Much magic here
 */
class Application {
    companion object : InjektMain() {

        @JvmStatic
        fun main(args: Array<String>) {
            initializeCli(args)
            onShutdown()
            run()
        }

        override fun InjektRegistrar.registerInjectables() {
            addLoggerFactory<Logger>({ name -> LoggerFactory.getLogger(name) },
                    { byClass -> LoggerFactory.getLogger(byClass) })
            val databaseUrl = "jdbc:derby:data/linkbait;create=true"
            addSingleton(JdbcConnectionSource(databaseUrl, DerbyEmbeddedDatabaseType()))
            addSingleton(ServerConfiguration())
        }

    }

}

/**
 * Logger
 */
val LOG: Logger by injectLogger(Application::class.java)

/**
 * Connection source for DB
 */
val connectionSource: JdbcConnectionSource = Injekt.get()

val configuration: ServerConfiguration = Injekt.get()

val webAppDirectory = File("linkbait-www")

/**
 * Do cleanup and other nice things here.
 */
fun onShutdown() {
    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        LOG.info("""

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                       Stopping Linkbait                        ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        """
        )

        Spark.stop();
        connectionSource.close()
    }))
}

/**
 *
 */
private fun run() {

    LOG.info(
            """
 __       __  .__   __.  __  ___ .______        ___       __  .___________.
|  |     |  | |  \ |  | |  |/  / |   _  \      /   \     |  | |           |
|  |     |  | |   \|  | |  '  /  |  |_)  |    /  ^  \    |  | `---|  |----`
|  |     |  | |  . `  | |    <   |   _  <    /  /_\  \   |  |     |  |
|  `----.|  | |  |\   | |  .  \  |  |_)  |  /  _____  \  |  |     |  |
|_______||__| |__| \__| |__|\__\ |______/  /__/     \__\ |__|     |__|

            """
    )

    LOG.info("""

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                       Starting Linkbait                        ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        """
    )

    initializeDatabase()
    initializeServer()
    initializeRoutes()

}

/**
 * Configure excellence into server
 */
fun initializeServer() {
    Spark.port(configuration.port)

    Spark.staticFiles.externalLocation(webAppDirectory.absolutePath)
//    Spark.externalStaticFileLocation(configuration.staticFileDirectory)
}

/**
 * Initialize the database and create any necessary tables
 */
fun initializeDatabase() {
    connectionSource.initialize()

    try {
        TableUtils.createTable(connectionSource, User::class.java)
    } catch (e: SQLException) {
        if ((e.cause as SQLException).sqlState == "X0Y32") {
            LOG.info("User Table already exists.")
        } else {
            LOG.error("Error creating tables", e)
        }
    }
}

/**
 * Setup paths for the RESTful service
 */
fun initializeRoutes() {

    val map = HashMap<String, String>()
    map.put("name", "Sam");


    Spark.before("*", addTrailingSlashes)

    Spark.get(INDEX, { request, response -> renderContent("index.html") })

    //Serve login page
    Spark.get(LOGIN, { request, response -> renderContent("login.html") })

    //Handle login
    Spark.post(LOGIN, handleLogin)

    //Handle signup
    Spark.get(SIGNUP, { request, response -> renderContent("signup.html") })

    Spark.post(SIGNUP, handleCreateUser)

    Spark.get(SEARCH, handleSearch)


    Spark.post(LOGOUT, { request, response -> "ok" })

    //Serve create page
    Spark.get(USER, { request, response -> renderContent("bot.html") })

}

/**
 * @param
 * @return
 */
fun renderContent(htmlFile: String): String? {
    try {
        val path = Paths.get(File("${webAppDirectory.absolutePath}/$htmlFile").toURI())
        return String(Files.readAllBytes(path), Charset.defaultCharset())
    } catch (e: IOException) {
    } catch (e: URISyntaxException) {
    }
    return null
}