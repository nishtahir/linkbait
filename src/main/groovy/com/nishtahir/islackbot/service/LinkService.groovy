package com.nishtahir.islackbot.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.islackbot.model.Link
import com.nishtahir.islackbot.util.TimestampUtils

/**
 * Service for manipulating links in database
 */
class LinkService {

    /**
     *
     */
    Dao<Link, Double> linkDao

    /**
     *
     * @param connectionSource Database connection source
     */
    LinkService(ConnectionSource connectionSource) {
        linkDao = DaoManager.createDao(connectionSource, Link.class)
    }

    /**
     *
     * @param timestamp
     * @param url
     * @param publisher
     * @return
     */
    def saveLink(String timestamp, String url, String publisher, String group, String channel) {
        linkDao.createIfNotExists(new Link(
                timestamp: Double.valueOf(timestamp),
                url: url,
                publisher: publisher,
                group: group,
                channel: channel));
    }

    /**
     *
     * @param link
     * @return
     */
    def saveLink(Link link){
        linkDao.createIfNotExists(link)
    }

    /**
     * Finds a single link
     * @param timestamp
     * @return
     */
    def findLink(String timestamp) {
        linkDao.queryForId(Double.valueOf(timestamp));
    }

    /**
     *
     * @param link
     * @return
     */
    def updateLink(Link link) {
        linkDao.update(link)
    }

    /**
     *
     * @param link
     * @return
     */
    def deleteLink(Link link) {
        linkDao.delete(link)
    }

    /**
     * @return links posted today.
     */
    List<Link> getLinksPostedToday() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentDay).prepare()
        return linkDao.query(query);
    }

    /**
     * @return list of links posted this week.
     */
    List<Link> getLinksPostedThisWeek() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentWeek).prepare()
        return linkDao.query(query);
    }

    /**
     * @return list of links posted this month.
     */
    List<Link> getLinksPostedThisMonth() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfCurrentMonth).prepare()
        return linkDao.query(query);
    }

}
