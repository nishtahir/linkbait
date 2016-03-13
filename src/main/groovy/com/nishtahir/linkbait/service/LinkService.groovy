package com.nishtahir.linkbait.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.util.TimestampUtils

/**
 * Service for manipulating links in database
 */
class LinkService {

    /**
     * Very zen.
     */
    Dao<Link, Double> linkDao

    /**
     * @param connectionSource Database connection source
     */
    LinkService(ConnectionSource connectionSource) {
        linkDao = DaoManager.createDao(connectionSource, Link.class)
    }

    /**
     * Attempts to save a link to the DB. If something was already there with the same timestamp
     * It returns that instead. Since slack identifies messages by timestamp, the chance of a collision
     * is pretty slim
     *
     * @param timestamp acts as id.
     * @param url link to save
     * @param publisher person that posted it
     * @return Item that was saved. If something else was there, that is returned instead.
     */
    Link saveLink(String timestamp, String url, User publisher, String group, String channel) {
        linkDao.createIfNotExists(new Link(
                timestamp: Double.valueOf(timestamp),
                url: url,
                publisher: publisher,
                group: group,
                channel: channel));
    }

    /**
     * Attempts to save a link to the DB. If something was already there with the same timestamp
     * It returns that instead. Since slack identifies messages by timestamp, the chance of a collision
     * is pretty slim
     *
     * @param link Link to save
     * @return Item that was saved. If something else was there, that is returned instead.
     */
    Link saveLink(Link link) {
        linkDao.createIfNotExists(link)
    }

    /**
     * @param timestamp id
     * @return
     */
    Link findLink(String timestamp) {
        linkDao.queryForId(Double.valueOf(timestamp));
    }

    /**
     *
     * @param link
     * @return
     */
    void updateLink(Link link) {
        linkDao.update(link)
    }

    /**
     *
     * @param link
     * @return
     */
    void deleteLink(Link link) {
        linkDao.delete(link)
    }

    /**
     *
     * @param timestamp
     * @return
     */
    Link upvoteLink(String timestamp) {
        Link link = findLink(timestamp)
        link?.upvotes++
        updateLink(link)
        return link
    }

    /**
     *
     * @param timestamp
     * @return
     */
    Link revokeUpvoteFromLink(String timestamp) {
        Link link = findLink(timestamp)
        link?.upvotes--
        updateLink(link)
        return link
    }

    /**
     *
     * @param timestamp
     * @return
     */
    Link downvoteLink(String timestamp) {
        Link link = findLink(timestamp)
        link?.downvotes++
        updateLink(link)
        return link
    }

    /**
     *
     * @param timestamp
     * @return
     */
    Link revokeDownvoteFromLink(String timestamp) {
        Link link = findLink(timestamp)
        link?.downvotes--
        updateLink(link)
        return link
    }

    /**
     * @return links posted today.
     */
    List<Link> getLinksPostedToday() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentDay).prepare()
        return linkDao.query(query).reverse()
    }

    /**
     * @return list of links posted this week.
     */
    List<Link> getLinksPostedThisWeek() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentWeek).prepare()
        return linkDao.query(query).reverse()
    }

    /**
     * @return list of links posted this month.
     */
    List<Link> getLinksPostedThisMonth() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentMonth).prepare()
        return linkDao.query(query).reverse()
    }

    /**
     * Returns a digest last week's top links sorted by votes
     * @return
     */
    List<Link> getWeeklyDigest() {
        List<Link> digest = new ArrayList<>()

        def builder = linkDao.queryBuilder()
        builder.limit(5L)
        builder.orderBy("upvotes", false)

        getDistinctChannels().each { channel ->
            def query = builder.where()
                    .ge("timestamp", TimestampUtils.getStartOfPreviousWeek())
                    .and()
                    .le("timestamp", TimestampUtils.getStartOfCurrentWeek())
                    .and()
                    .eq("channel", channel)
                    .prepare()
            digest.addAll(linkDao.query(query))
        }
        return digest
    }

    List<String> getDistinctChannels() {
        List<String> channels = new ArrayList<>()
        def query = linkDao.queryBuilder().distinct().selectColumns("channel").prepare()
        linkDao.query(query).each {
            channels.add(it.channel)
        }
        return channels
    }
}
