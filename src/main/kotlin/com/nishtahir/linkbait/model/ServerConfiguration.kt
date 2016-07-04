package com.nishtahir.linkbait.model

import com.nishtahir.linkbait.core.model.Configuration
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

    override fun getTemporaryFileDirectory(): File? {
        return File(temporaryFileDirectory)
    }

    override fun getPluginRepository(): File? {
        return File(pluginRepository)
    }

    override fun getStaticFileDirectory(): File? {
        return File(staticFileDirectory)
    }

    override fun getPluginDirectory(): File? {
        return File(pluginDirectory)
    }
}