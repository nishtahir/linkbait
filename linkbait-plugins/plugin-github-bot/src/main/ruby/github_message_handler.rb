require 'java'
require 'colorize'

java_package 'com.nishtahir.linkbait.github'

java_import 'com.nishtahir.linkbait.plugin.Plugin'
java_import 'com.nishtahir.linkbait.plugin.PluginContext'
java_import 'com.nishtahir.linkbait.plugin.MessageEventListener'
java_import 'com.nishtahir.linkbait.plugin.MessageEvent'

java_import 'org.jetbrains.annotations.NotNull'

class GithubMessageHandler
    java_implements MessageEventListener

  java_annotation 'Override'
  java_signature 'void handleMessageEvent(MessageEvent)'
  def handleMessageEvent(event)
    puts event.getMessage().blue
  end

  def setContext(_context)
    puts "Setting context in handler"
    @context = _context
  end
end
