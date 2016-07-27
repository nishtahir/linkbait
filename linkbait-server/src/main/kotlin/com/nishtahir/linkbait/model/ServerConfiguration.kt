package com.nishtahir.linkbait.model

import com.nishtahir.linkbait.plugin.model.Configuration
import java.io.File


class ServerConfiguration : Configuration {

    /**
     * For temporary file storage
     */
    var temporaryFileDirectory = "data/tmp"

    /**
     * Plugins downloaded as artifacts as well as their
     * dependencies will be stored here
     */
    var pluginRepository = "data/repo"

    /**
     * Static files to be served will be stored here
     */
    var staticFileDirectory = "data/static"

    /**
     * External plugins should be kept here
     */
    var pluginDirectory = "data/plugins"

    /**
     * Port number to serve on
     */
    var port = 4567

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
}