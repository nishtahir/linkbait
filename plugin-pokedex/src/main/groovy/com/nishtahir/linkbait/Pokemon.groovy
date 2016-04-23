package com.nishtahir.linkbait

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includePackage = false)
@DatabaseTable
class Pokemon {

    /**
     * National dex id number
     */
    @DatabaseField
    String id

    @DatabaseField
    String name

    @DatabaseField
    String type0

    @DatabaseField
    String type1

    @DatabaseField
    String description

    @DatabaseField
    String thumbnail

    @DatabaseField
    int color_id


}
