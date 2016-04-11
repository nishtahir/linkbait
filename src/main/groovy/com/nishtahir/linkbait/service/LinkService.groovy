package com.nishtahir.linkbait.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.linkbait.model.Link
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.util.TimestampUtils
import org.apache.tika.metadata.Metadata
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.html.HtmlParser
import org.apache.tika.sax.BodyContentHandler

/**
 * Service for manipulating links in database
 */
class LinkService {

    private static int MAX_DESC_LENGTH = 450;

    public
    static String USER_AGENT = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36'

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
        Link link = new Link(
                timestamp: Double.valueOf(timestamp),
                url: url,
                publisher: publisher,
                group: group,
                channel: channel)

        Map meta = parseUrlMetadata(url)

        link.setTitle(meta.get('title'))
        link.setDescription(meta.get('description'))
        saveLink(link)
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

    public Map parseUrlMetadata(String url) {
        Metadata metadata = new Metadata();
        InputStream inputStream = new ByteArrayInputStream(url.toURL().getText(requestProperties: ['User-Agent': USER_AGENT]).bytes)

        String title = ""
        String description = ""
        try {
            new HtmlParser().parse(inputStream, new BodyContentHandler(), metadata, new ParseContext())
            title = getMetadataTitle(metadata)
            description = getMetadataDescription(metadata)
        } catch (Exception ignore) {

        }

        return ['title': title, 'description': description]
    }

    /**
     *
     * @param metadata
     * @return
     */
    public String getMetadataTitle(Metadata metadata) {
        if (metadata.get('twitter:title') != null) {
            return metadata.get('twitter:title')
        } else if (metadata.get(TikaCoreProperties.TITLE) != null) {
            return metadata.get(TikaCoreProperties.TITLE)
        } else {
            return null
        }
    }

    /**
     *
     * @param metadata
     * @return
     */
    public String getMetadataDescription(Metadata metadata) {
        String description;
        if (metadata.get('twitter:description') != null) {
            description = metadata.get('twitter:description')
        } else if (metadata.get('description') != null) {
            description = metadata.get('description')
        } else if (metadata.get(TikaCoreProperties.DESCRIPTION) != null) {
            description = metadata.get(TikaCoreProperties.DESCRIPTION)
        } else {
            return null
        }

        return (description.length() > MAX_DESC_LENGTH) ? description.take(MAX_DESC_LENGTH) + "..." : description
    }

    def addTitleAndDescriptionToTable() {
        linkDao.executeRaw("ALTER TABLE `link` ADD COLUMN title STRING;")
        linkDao.executeRaw("ALTER TABLE `link` ADD COLUMN description STRING;")
    }
}
