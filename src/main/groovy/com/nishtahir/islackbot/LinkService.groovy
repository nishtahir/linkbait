package com.nishtahir.islackbot

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.islackbot.util.TimestampUtils

/**
 * Created by nish on 2/19/16.
 */
class LinkService {

    Dao<Link, Double> linkDao

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
    def saveLink(String timestamp, String url, String publisher) {
        linkDao.createIfNotExists(new Link(timestamp: Double.valueOf(timestamp), url: url, publisher: publisher));
    }

    def saveLink(Link link){
        linkDao.createIfNotExists(link)
    }

    def findLink(String timestamp) {
        linkDao.queryForId(Double.valueOf(timestamp));
    }

    def updateLink(Link link) {
        linkDao.update(link)
    }


    def deleteLink(Link link) {
        linkDao.delete(link)
    }

    List<Link> getLinksPostedToday() {
        def query = linkDao.queryBuilder()
                .where().ge("timestamp", TimestampUtils.startOfDayToday).prepare()

        return linkDao.query(query);
    }

}
