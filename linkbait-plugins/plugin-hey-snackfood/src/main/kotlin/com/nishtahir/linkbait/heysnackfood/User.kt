package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable
class User {

    @DatabaseField(generatedId = true)
    var id: Int = 0

    @DatabaseField
    var name: String = ""

    @DatabaseField
    var count: Int = 0

    fun incrementCount(count: Int = 1) {
        this.count += count
    }
}