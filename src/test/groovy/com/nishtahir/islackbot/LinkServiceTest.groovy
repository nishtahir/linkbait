package com.nishtahir.islackbot

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import spock.lang.Specification

/**
 * Created by nish on 2/20/16.
 */
class LinkServiceTest extends Specification {

    LinkService linkService
    ConnectionSource connectionSource

    void setup() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource('jdbc:sqlite:islack-bot-test.sqlite')
        TableUtils.createTableIfNotExists(connectionSource, Link.class)

        linkService = new LinkService(connectionSource)
    }

    void cleanup() {
        connectionSource.close()
    }

    def "SaveLink"() {
        given:
            def link = new Link()
            link.with {
                (timestamp, url, publisher) = ['1234', 'http://example.com', 'testAuthor']
            }

        expect:
            link == linkService.saveLink('1234', 'http://example.com', 'testAuthor')
    }

    def "GetLinksPostedToday"() {
        given:
        linkService.saveLink(new Link(Calendar.getInstance().getTimeInMillis(), 'http://test.com', 'testAuthor'))
        linkService.saveLink(new Link(Calendar.getInstance().getTimeInMillis(), 'http://test2.com', 'testAuthor2'))
        Link badTest = new Link(1234, 'http://test2.com', 'testAuthor2')
        linkService.saveLink(badTest)

        List<Link> result = linkService.linksPostedToday

        expect:
        result.size() == 2
        !result.contains(badTest)
    }
}
