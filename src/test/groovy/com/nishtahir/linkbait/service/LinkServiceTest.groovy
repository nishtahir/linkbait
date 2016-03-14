package com.nishtahir.linkbait.service

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import spock.lang.Specification

class LinkServiceTest extends Specification {

    LinkService linkService
    ConnectionSource connectionSource

    void setup() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource('jdbc:sqlite:islack-bot-test.sqlite')
        TableUtils.dropTable(connectionSource, Link.class, true)
        TableUtils.createTableIfNotExists(connectionSource, Link.class)

        linkService = new LinkService(connectionSource)
    }

    void cleanup() {
        connectionSource.close()
    }

    def "SaveLink_WithSimpleLink_SavesCorrectly"() {
        given:
        User testUser = new User(username:"Test User", upvotes:0, downvotes:0)
        def link = new Link()
        link.with {
            (timestamp, url, publisher) = [1234, 'http://example.com', testUser]
        }

        expect:
        link == linkService.saveLink(link)
    }

    def "GetLinksPostedToday_WithBadRecord_ReturnsCorrectValues"() {
        given:
        User testUser = new User(username:"Test User", upvotes:0, downvotes:0)
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), url:'http://test.com', publisher:testUser))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), url:'http://test2.com', publisher:testUser))

        Link recordWithTimestampThatIsntToday = new Link(1234, 'http://test2.com', testUser)
        linkService.saveLink(recordWithTimestampThatIsntToday)

        List<Link> result = linkService.linksPostedToday

        expect:
        result.size() == 2
        !result.contains(recordWithTimestampThatIsntToday)
    }

    def "GetDistinctChannels_WithAllInputInSingleChannel_ReturnsOneChannel"(){
        when:
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "test"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "test"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "test"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "test"))

        List<String> channels = linkService.getDistinctChannels()


        then:
        channels.size() == 1
        channels.get(0) == "test"
    }

    def "GetDistinctChannels_WithMultipleChannels_ReturnsCorrectNumberOfChannels"(){
        when:
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "do"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "you"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "feel"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "in"))
        linkService.saveLink(new Link(timestamp:Calendar.getInstance().getTimeInMillis(), channel: "charge"))

        List<String> channels = linkService.getDistinctChannels()


        then:
        channels.size() == 5
        channels.sort() == ['do','you','feel','in','charge'].sort()
    }
}
