package com.nishtahir.linkbait.cli

import com.beust.jcommander.Parameter
import com.nishtahir.linkbait.ApplicationConfiguration
import com.nishtahir.linkbait.extensions.DEFAULT_PLUGIN_DIRECTORY
import com.nishtahir.linkbait.extensions.DEFAULT_REPOSITORY
import com.nishtahir.linkbait.extensions.DEFAULT_STATIC_FILE_DIRECTORY
import com.nishtahir.linkbait.extensions.DEFAULT_TEMP_FILE_DIRECTORY
import com.nishtahir.linkbait.plugin.model.Configuration

/**
 * Created by nish on 7/30/16.
 */
abstract class AbstractFileCommand {

    @Parameter(names = arrayOf("--plugin", "Plugin directory"))
    var pluginDir: String = DEFAULT_PLUGIN_DIRECTORY;

    @Parameter(names = arrayOf("--static", "Static file directory"))
    var staticDir: String = DEFAULT_STATIC_FILE_DIRECTORY;

    @Parameter(names = arrayOf("--repo", "Plugin repository directory"))
    var repoDir: String = DEFAULT_REPOSITORY;

    @Parameter(names = arrayOf("--temp", "Temporary file directory"))
    var tempDir: String = DEFAULT_TEMP_FILE_DIRECTORY;

    open fun getConfiguration(): Configuration {
        val config = ApplicationConfiguration()

        config.pluginDirectory = pluginDir
        config.pluginRepository = repoDir
        config.staticFileDirectory = staticDir
        config.temporaryFileDirectory = tempDir

        return config
    }
}