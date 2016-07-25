package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Gotta catch em' all!
 */
@DatabaseTable
class Pokemon(

        /**
         * National dex id number
         */
        @DatabaseField
        var id: String = "",

        @DatabaseField
        var name: String = "",

        @DatabaseField
        var weight: Int = 0,

        @DatabaseField
        var height: Int = 0,

        @DatabaseField
        var color: String = "",

        @DatabaseField
        var abilities: String = "",

        @DatabaseField
        var type: String = "",

        @DatabaseField
        var description: String = "",

        @DatabaseField
        var thumbnail: String = ""

) {
    fun getAbilities(): List<String> {
        return abilities.split(';').filter {
            !it.isNullOrBlank()
        }
    }

    fun getType(): List<String> {
        return type.split(';').filter {
            !it.isNullOrBlank()
        }
    }
}

