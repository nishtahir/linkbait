require 'java'
require 'colorize'

java_package 'com.nishtahir.linkbait.github'

# You have to explicitly import all java classes you use.
# Even those in the same package.
java_import 'com.nishtahir.linkbait.github.GithubMessageHandler'

java_import 'com.nishtahir.linkbait.plugin.Plugin'
java_import 'com.nishtahir.linkbait.plugin.PluginContext'
java_import 'com.nishtahir.linkbait.plugin.MessageEventListener'

java_import 'org.jetbrains.annotations.NotNull'

class GithubPlugin
    java_implements Plugin

  def initialize
    @handler = GithubMessageHandler.new
  end

  java_annotation 'Override'
  java_signature 'void start(PluginContext)'
  def start(context)
   puts "Starting with context"
   @handler.setContext(context)
   context.registerListener(@handler)
  end

  java_annotation 'Override'
  java_signature 'void stop(PluginContext)'
  def stop(context)
    puts "Stopping"
    context.unregisterListener(@handler)
  end

  java_annotation 'Override'
  java_signature 'void onPluginStateChanged()'
  def onPluginStateChanged()
  end

  def test_octokit()
    puts "Ok finally some progress".green
  end

  def getHandler()
    @handler
  end
end
