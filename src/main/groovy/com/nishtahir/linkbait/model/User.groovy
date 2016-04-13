package com.nishtahir.linkbait.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@DatabaseTable
@ToString(includePackage = false)
class User {

    @DatabaseField(id = true)
    String slackUserId

    @DatabaseField
    String username

    @DatabaseField
    long upvotes

    @DatabaseField
    long downvotes

}
