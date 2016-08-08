package com.nishtahir.linkbait.extensions

import com.nishtahir.linkbait.ApplicationConfiguration
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

import org.apache.commons.cli.*

val helpOption = Option.builder("h")
        .desc("Show this help message.")
        .longOpt("help")
        .build()

val versionOption = Option.builder("v")
        .desc("Display version information.")
        .longOpt("version")
        .build()

val serverOption = Option.builder("s")
        .desc("Start the application server")
        .longOpt("server")
        .build()

val botOption = Option.builder("b")
        .desc("Start a single bot. Requires api key and id")
        .longOpt("bot")
        .build()

val apiKeyOption = Option.builder()
        .desc("Start a single bot. Requires api key and id")
        .longOpt("key")
        .build()

val idOption = Option.builder()
        .desc("Identifier for bot")
        .longOpt("id")
        .build()

val serviceOption = Option.builder()
        .desc("Identifier for bot")
        .longOpt("service")
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

val rootOptions = Options();

val serverOptions = Options();

val botOptions = Options();

var arguments: Array<String> = emptyArray()

/**
 * @param args commandline arguments
 */
fun initializeCli(args: Array<String>): ApplicationConfiguration {
    arguments = args

    val folderParams = OptionGroup()
    folderParams.addOption(staticFileDirectoryOption)
    folderParams.addOption(tempFileDirectoryOption)
    folderParams.addOption(pluginDirectoryOption)
    folderParams.addOption(repositoryDirectoryOption)
    folderParams.isRequired = false

    serverOptions.addOptionGroup(folderParams)


    val launchConfigurationOption = OptionGroup()
    launchConfigurationOption.addOption(helpOption)
    launchConfigurationOption.addOption(serverOption)
    launchConfigurationOption.addOption(botOption)
    launchConfigurationOption.isRequired = true

    rootOptions.addOptionGroup(launchConfigurationOption)


    botOptions.addOptionGroup(launchConfigurationOption)
    botOptions.addOptionGroup(folderParams)

    val parser = DefaultParser();
    return handleRootOptions(parser.parse(rootOptions, arguments, true))
}


/**
 * @param cmd parsed commandline
 * Parse arguments and update the configuration
 */
fun handleRootOptions(cmd: CommandLine): ApplicationConfiguration {

    if (cmd.hasOption(helpOption.opt)) {
        help()
    }

    if (cmd.hasOption(serverOption.opt)) {
        return handleServerOptions(cmd)
    }

    if (cmd.hasOption(botOption.opt)){
        return handleBotOptions(cmd)
    }

    return ApplicationConfiguration()
}

fun handleServerOptions(cmd: CommandLine): ApplicationConfiguration {
    val config: ApplicationConfiguration = ApplicationConfiguration()

    getFolderParams(cmd, config)

    return config
}

fun handleBotOptions(cmd: CommandLine): ApplicationConfiguration {
    val config: ApplicationConfiguration = ApplicationConfiguration()

    val botCmd = DefaultParser().parse(botOptions, cmd.args, false)

    getFolderParams(botCmd, config)

    return config
}

fun getFolderParams(cmd: CommandLine, config: ApplicationConfiguration) {

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
    formatter.printHelp("linkbait", rootOptions, true);
    System.exit(0)
}