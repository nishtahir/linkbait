package com.nishtahir.islackbot

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource

/**
 * Created by nish on 2/19/16.
 */
class LinkService {

    Dao<Link, String> linkDao

    LinkService(ConnectionSource connectionSource){
        linkDao = DaoManager.createDao(connectionSource, Link.class)
    }

    /**
     *
     * @param timestamp
     * @param url
     * @param publisher
     * @return
     */
    def saveLink(String timestamp, String url, String publisher){
        linkDao.createIfNotExists(new Link(timestamp: timestamp, url: url, publisher: publisher));
    }

    def findLink(String timestamp){
        linkDao.queryForId(timestamp);
    }

    def updateLink(Link link){
        linkDao.update(link)
    }


    def deleteLink(Link link){
        linkDao.delete(link)
    }
}
