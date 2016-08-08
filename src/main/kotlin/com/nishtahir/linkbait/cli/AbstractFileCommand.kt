package com.nishtahir.linkbait.cli

import com.beust.jcommander.Parameter
import com.nishtahir.linkbait.ApplicationConfiguration
import com.nishtahir.linkbait.plugin.model.Configuration

/**
 * Created by nish on 7/30/16.
 */
abstract class AbstractFileCommand {

    @Parameter(names = arrayOf("--plugin", "Plugin directory"))
    var pluginDir: String = "plugins/";

    @Parameter(names = arrayOf("--static", "Static file directory"))
    var staticDir: String = "static/";

    @Parameter(names = arrayOf("--repo", "Plugin repository directory"))
    var repoDir: String = "repo/";

    @Parameter(names = arrayOf("--temp", "Temporary file directory"))
    var tempDir: String = "temp/";

    open fun getConfiguration(): Configuration {
        val config = ApplicationConfiguration()

        config.pluginDirectory = pluginDir
        config.pluginRepository = repoDir
        config.staticFileDirectory = staticDir
        config.temporaryFileDirectory = tempDir

        return config
    }
}