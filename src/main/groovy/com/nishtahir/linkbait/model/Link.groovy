package com.nishtahir.linkbait.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical
import groovy.transform.Sortable

@Canonical
@DatabaseTable
class Link {

    @DatabaseField( id = true)
    double timestamp;

    @DatabaseField
    String url;

    @DatabaseField(foreign = true, foreignAutoRefresh=true)
    User publisher;

    @DatabaseField
    long upvotes;

    @DatabaseField
    long downvotes;

    @DatabaseField
    String channel;

    @DatabaseField
    String group;

}
