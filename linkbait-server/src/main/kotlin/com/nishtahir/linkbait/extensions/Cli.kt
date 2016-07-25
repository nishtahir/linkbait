package com.nishtahir.linkbait.extensions

import com.nishtahir.linkbait.model.ServerConfiguration
import org.apache.commons.cli.*
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

val helpOption = Option.builder("h")
        .desc("Show this help message.")
        .longOpt("help")
        .build()

val portOption = Option.builder("p")
        .desc("Select a port to run on")
        .longOpt("port")
        .argName("number")
        .hasArg()
        .build()

val staticFileDirectoryOption = Option.builder()
        .desc("Static file directory")
        .longOpt("static-dir")
        .argName("directory")
        .hasArg()
        .build()

val tempFileDirectoryOption = Option.builder()
        .desc("Temp file directory")
        .longOpt("temp-dir")
        .argName("directory")
        .hasArg()
        .build()

val pluginDirectoryOption = Option.builder()
        .desc("Plugin file directory")
        .longOpt("plugin-dir")
        .argName("directory")
        .hasArg()
        .build()

val repositoryDirectoryOption = Option.builder()
        .desc("Repo directory")
        .longOpt("repo-dir")
        .argName("directory")
        .hasArg()
        .build()

val options = Options();

/**
 * @param args commandline arguments
 */
fun initializeCli(args: Array<String>) {

    options.addOption(helpOption);
    options.addOption(portOption);
    options.addOption(staticFileDirectoryOption);
    options.addOption(tempFileDirectoryOption);
    options.addOption(pluginDirectoryOption);
    options.addOption(repositoryDirectoryOption);

    val parser = DefaultParser();
    handleCli(parser.parse(options, args))
}

/**
 * @param cmd parsed commandline
 * Parse arguments and update the configuration
 */
fun handleCli(cmd: CommandLine) {

    if (cmd.hasOption(helpOption.opt)) {
        help()
    }

    val config = Injekt.get<ServerConfiguration>()

    cmd.getOptionValue(portOption.opt)?.let {
        config.port = it.toInt()
    }

    cmd.getOptionValue(staticFileDirectoryOption.longOpt)?.let {
        config.staticFileDirectory = it
    }

    cmd.getOptionValue(pluginDirectoryOption.longOpt)?.let {
        config.pluginDirectory = it
    }

    cmd.getOptionValue(repositoryDirectoryOption.longOpt)?.let {
        config.pluginRepository = it
    }

    cmd.getOptionValue(tempFileDirectoryOption.longOpt)?.let {
        config.temporaryFileDirectory = it
    }

}

/**
 * Display help information
 */
fun help() {
    val formatter = HelpFormatter();
    formatter.printHelp("linkbait", options);
    System.exit(0)
}