package com.nishtahir.linkbait

import com.nishtahir.linkbait.extensions.*
import com.nishtahir.linkbait.plugin.model.Configuration
import java.io.File


class ApplicationConfiguration : Configuration {

    /**
     * For temporary file storage
     */
    var temporaryFileDirectory = DEFAULT_TEMP_FILE_DIRECTORY

    /**
     * Plugins downloaded as artifacts as well as their
     * dependencies will be stored here
     */
    var pluginRepository = DEFAULT_REPOSITORY

    /**
     * Static files to be served will be stored here
     */
    var staticFileDirectory = DEFAULT_STATIC_FILE_DIRECTORY

    /**
     * External plugins should be kept here
     */
    var pluginDirectory = DEFAULT_PLUGIN_DIRECTORY

    /**
     * Data stuff
     */
    var dataDirectory = DEFAULT_DATA_DIRECTORY
    /**
     * Port number to serve on
     */
    var port = DEFAULT_PORT

    override fun getTemporaryFileDirectory(): File {
        val dir = File(temporaryFileDirectory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    override fun getPluginRepository(): File {
        val dir = File(pluginRepository)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    override fun getStaticFileDirectory(): File {
        val dir = File(staticFileDirectory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    override fun getPluginDirectory(): File {
        val dir = File(pluginDirectory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    override fun getDataDirectory(): File {
        val dir = File(dataDirectory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
}