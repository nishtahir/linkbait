package com.nishtahir.linkbait.plugin

import org.apache.commons.dbcp2.BasicDataSource
import javax.sql.DataSource

/**
 * Factory method to provide database connection instances. This prevents a clash when
 * loading drivers using the Driver manager
 * @param type See Type for supported db drivers
 * @param username db user
 * @param password db password
 */
fun DataSource(type: Type, url: String, username: String, password: String): DataSource {
    val datasource = BasicDataSource()
    when (type) {
        Type.SQLITE -> {
            datasource.driverClassName = "org.sqlite.JDBC"
        }
        Type.DERBY_EMBEDDED -> {
            datasource.driverClassName = "org.apache.derby.jdbc.EmbeddedDriver"
        }
    }
    datasource.url = url
    datasource.username = username
    datasource.password = password

    return datasource
}

/**
 * Type of Data storage
 */
enum class Type {

    /**
     *
     */
    SQLITE,

    /**
     *
     */
    DERBY_EMBEDDED
}