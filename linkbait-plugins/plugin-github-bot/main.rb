require 'java'

java_import 'com.nishtahir.linkbait.github.GithubPlugin'
java_import 'com.nishtahir.linkbait.plugin.PluginContext'

java_import com.nishtahir.linkbait.plugin.MessageEvent
java_import com.nishtahir.linkbait.plugin.MessageEventListener

java_import 'com.nishtahir.linkbait.test.MockContext'
java_import 'com.nishtahir.linkbait.test.MockMessenger'
java_import 'com.nishtahir.linkbait.test.MockConfiguration'

puts $CLASSPATH
puts "> Running with Ruby: #{RUBY_VERSION} on #{RUBY_PLATFORM}"

configuration = MockConfiguration.new
puts "Config inited"

messenger = MockMessenger.new
puts "Messenger inited"

context = MockContext.new(configuration, messenger)
puts "Context ok"

plugin = GithubPlugin.new
plugin.start(context)

event = MessageEvent.new
event.setMessage("https://github.com/nishtahir/alang")
plugin.getHandler().handleMessageEvent(event)
