package com.nishtahir.linkbait.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 * Created by nish on 7/30/16.
 */
@Parameters(commandDescription = "Start a linkbait server instance", commandNames = arrayOf("server"))
class CommandServer : AbstractFileCommand() {

    @Parameter(names = arrayOf("--port", "-p"), description = "Port number")
    var port: Int = 4567;

}