package com.nishtahir.islackbot.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

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
