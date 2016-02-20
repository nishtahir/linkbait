package com.nishtahir.islackbot

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical

@Canonical
@DatabaseTable
class Link {

    @DatabaseField( id = true)
    double timestamp;

    @DatabaseField
    String url;

    @DatabaseField
    String publisher;

    @DatabaseField
    long upvotes;

    @DatabaseField
    long downvotes;
}
