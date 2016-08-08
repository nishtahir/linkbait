package com.nishtahir.linkbait.cli

import com.beust.jcommander.Parameter

/**
 *
 */
class CommandLinkbait {

    @Parameter(names = arrayOf("--help", "-h", "Display this message"), help = true)
    var help: Boolean? = null;

}