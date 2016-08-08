package com.nishtahir.linkbait.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.nishtahir.linkbait.plugin.model.Configuration

/**
 * Created by nish on 7/30/16.
 */
@Parameters(commandDescription = "Start a standalone bot instance", commandNames = arrayOf("bot"))
class CommandBot : AbstractFileCommand(){

    @Parameter(names = arrayOf("--id", "i", "Bot identifier"), required = true)
    var id: String? = null

    @Parameter(names = arrayOf("--service", "s", "Service to connect to"), required = true)
    var service: String? = null

    @Parameter(names = arrayOf("--key", "k", "Service api key or token"), required = true)
    var apiKey: String? = null

}