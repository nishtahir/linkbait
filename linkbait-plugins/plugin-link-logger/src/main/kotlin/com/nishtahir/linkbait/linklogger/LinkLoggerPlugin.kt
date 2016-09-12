package com.nishtahir.linkbait.linklogger

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.logger.LoggerFactory
import com.j256.ormlite.table.DatabaseTable
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.plugin.*
import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import java.sql.SQLException

/**
 * Pokedex plugin for Linkbait. To edit the pokedex content.
 * Edit the resource in resources/pokedex.sqlite
 *
 * Disclaimer:
 * Official pokedex resource is http://www.pokemon.com/us/pokedex/
 * All Pokémon content is © Nintendo, Game Freak, and The Pokémon Company.
 *
 */

class LinkLoggerPlugin : Plugin() {

    /**
     *
     */
    var linkLoggerHandler: LinkLoggerHandler? = null

    companion object {
        init {
            Class.forName("org.sqlite.JDBC")
            InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:data/link-logger.sqlite"))
            InjektModule.scope.addFactory { LinkLoggerService() }
        }
    }

    override fun start(context: PluginContext) {
        linkLoggerHandler = LinkLoggerHandler(context)
        linkLoggerHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        linkLoggerHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }

}

class LinkLoggerService() {

    val connectionSource: JdbcConnectionSource = InjektModule.scope.get()

    val linkDao: Dao<Link, Int> = DaoManager.createDao(connectionSource, Link::class.java)

    init {
        TableUtils.createTableIfNotExists(connectionSource, Link::class.java)
    }

    fun findLink(url: String): Link? {
        return linkDao.queryBuilder()
                .where()
                .eq(Link::url.name, url)
                .queryForFirst()
    }

    fun createLink(link: Link) {
        try {
            linkDao.create(link)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun findOrCreateLink(url: String): Link {
        var link = findLink(url)
        if (link == null) {
            link = Link(url = url)
            createLink(link)
        }
        return link
    }

    fun updateLink(link: Link) {
        linkDao.createOrUpdate(link)
    }

}

class LinkLoggerHandler(val context: PluginContext) : MessageEventListener, ReactionEventListener {

    val URL_PATTERN = """(?<url>((https?|s?ftp):\/\/)?([\da-z\.:@-]+)\.([a-z\.]{2,6})([\/\w\-&\?:@=%+\.#\(\)]*)*\/?)""".toRegex()

    override fun handleMessageEvent(event: MessageEvent) {

    }

    override fun handleReactionEvent(event: ReactionEvent) {

    }

    fun getLinksFromString(string: String): List<String> {
        return URL_PATTERN.findAll(string).map { it.value }.toList()
    }

}

@DatabaseTable data class Link(@DatabaseField(generatedId = true) var id: Long = 0L,
                               @DatabaseField var url: String = "",
                               @DatabaseField val author: String = "",
                               @DatabaseField val conversation: String = "",
                               @DatabaseField val upvotes: Int = 0,
                               @DatabaseField val downvotes: Int = 0)

object InjektModule {
    @Volatile var scope: InjektScope = InjektScope(DefaultRegistrar())
}


