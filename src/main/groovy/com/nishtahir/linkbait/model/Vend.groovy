package com.nishtahir.linkbait.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical

@Canonical
@DatabaseTable
class Vend {
    enum Rarity {
        COMMON,
        UNCOMMON,
        RARE
    }

    @DatabaseField(generatedId = true)
    int id

    @DatabaseField(unique = true)
    String item

    @DatabaseField
    Rarity rarity

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    User publisher
}
