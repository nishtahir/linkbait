package com.nishtahir.linkbait.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical

@Canonical
@DatabaseTable
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
