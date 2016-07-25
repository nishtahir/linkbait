package com.nishtahir.linkbait.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * User information
 */
@DatabaseTable
class User {

    constructor() {
    }

    constructor(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    @DatabaseField(generatedId = true)
    var id: Long = 0L

    @DatabaseField
    var name: String = ""

    @DatabaseField(unique = true)
    var email: String = ""

    @DatabaseField
    @JsonIgnore
    var password: String = ""
}