package de.langerhans.linkbait.tags

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils

/**
 * Created by maxke on 16.08.2016.
 * Tags DB access
 */
class TagService {

    private val connectionSource: JdbcConnectionSource = InjektModule.scope.get()
    private val dao: Dao<Tag, Int> = DaoManager.createDao(connectionSource, Tag::class.java)

    init {
        createDatabaseIfNeeded()
    }

    private fun createDatabaseIfNeeded() {
        TableUtils.createTableIfNotExists(connectionSource, Tag::class.java)
    }

    /**
     * returns true if it was a new tag
     */
    fun saveTag(channel: String, addedBy: String, tag: String, value: String): Boolean {
        val thisTag = findTagByName(channel, tag) ?: Tag()

        thisTag.channel = channel
        thisTag.addedBy = addedBy
        thisTag.tag = tag
        thisTag.value = value
        thisTag.lastUpdate = System.currentTimeMillis()

        return dao.createOrUpdate(thisTag).isCreated
    }

    fun findTagByName(channel: String, tag: String): Tag? {
        return dao.queryBuilder().where()
                .eq("channel", channel)
                .and().eq("tag", tag)
                .queryForFirst()
    }

    fun deleteTagByName(channel: String, tag: String): Boolean {
        val thisTag = findTagByName(channel, tag) ?: return false
        return dao.delete(thisTag) > 0
    }

    fun allTagsByChannel(channel: String): List<Tag> {
        return dao.queryBuilder().where()
                .eq("channel", channel)
                .query()
    }
}