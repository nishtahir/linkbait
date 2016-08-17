package de.langerhans.linkbait.tags

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by maxke on 16.08.2016.
 * Tag DAO
 */
@DatabaseTable
class Tag {

    @DatabaseField(generatedId = true)
    var id: Int = 0

    @DatabaseField
    var channel: String = ""

    @DatabaseField
    var tag: String = ""

    @DatabaseField
    var value: String = ""

    @DatabaseField
    var addedBy: String = ""

    @DatabaseField
    var lastUpdate: Long = 0
}