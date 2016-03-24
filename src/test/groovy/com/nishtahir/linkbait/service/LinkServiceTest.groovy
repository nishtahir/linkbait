package com.nishtahir.linkbait.service

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import org.apache.tika.metadata.Metadata
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.html.HtmlParser
import org.apache.tika.sax.BodyContentHandler
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

    def "BoilerPipeTest"(){
        when:
//        URL url = new URL('http://www.theverge.com/2016/3/23/11293576/google-cloud-machine-learning-platform-available-developers')
//        URL url = new URL('https://github.com/androidchat/androidchat.github.io')
        URL url = new URL('http://www.engadget.com/2016/03/23/xkcd-creator-contributes-to-school-textbooks/')
//        final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR
//        final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();
//        println hh.process(url, extractor)
//        InputSource inputSource = new InputSource( new StringReader( url.text ) );

        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        InputStream inputstream = new ByteArrayInputStream(url.text.bytes);
        ParseContext pcontext = new ParseContext();

        //Html parser
        HtmlParser htmlparser = new HtmlParser();
        htmlparser.parse(inputstream, handler, metadata,pcontext);

        println metadata.get(TikaCoreProperties.TITLE)
        println metadata.get("description")

        println metadata.get(TikaCoreProperties.DESCRIPTION)
//        System.out.println("Contents of the document:" + handler.toString());
//        System.out.println("Metadata of the document:");
//        String[] metadataNames = metadata.names();

//        for(String name : metadataNames) {
//            System.out.println(name + ":   " + metadata.get(name));
//        }

//        final TextDocument doc = new BoilerpipeSAXInput(inputSource).getTextDocument();

//        println "Page title: " + doc.title
//        println doc.content.take(200)
        then:
        true
    }
}
