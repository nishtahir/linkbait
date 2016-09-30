require 'java'
require 'colorize'
require 'octokit'

java_package 'com.nishtahir.linkbait.github'

java_import 'com.nishtahir.linkbait.plugin.Plugin'
java_import 'com.nishtahir.linkbait.plugin.PluginContext'
java_import 'com.nishtahir.linkbait.plugin.MessageEventListener'
java_import 'com.nishtahir.linkbait.plugin.MessageEvent'

java_import 'org.jetbrains.annotations.NotNull'

# So the general idea: Someone pastes a link to github. It would be nice to use
# The Octokit github api wrapper to fetch some nice info from github about the repo.
# Plugin should form an attachment and send it back.
#
# Example,
#
# input:
#   http://github.com/nishtahir/ALang
#
class GithubMessageHandler
    java_implements MessageEventListener

  java_annotation 'Override'
  java_signature 'void handleMessageEvent(MessageEvent)'
  def handleMessageEvent(event)
    if match = event.getMessage().match(/https?:\/\/github.com\/(?<username>[a-zA-Z0-9]+)(\/(?<repository>([a-zA-Z0-9]+)))?/)
      user = Octokit.user match[:username]
      puts "#{user.name}".blue
      puts "#{user.bio}".yellow
      puts "#{user.public_repos}"

      name = user.name
      bio = user.bio.to_s.empty? ? user.bio : "A random awesome developer"
      avatar = user.avatar_url
      titleUrl = user.html_url


      attachment = Attachment(
        name,
        bio,
        "000000",
        titleUrl,
        avatar,
        nil
      )

      @context.sendAttachment(event.getChannel(), attachment)

    # elsif match = event.getMessage().match(/https?:\/\/github.com\/(?<username>[a-zA-Z0-9]+)\/(?<repository>([a-zA-Z0-9]+))/)
    #
    #   :public_repos=>23,
    #   :public_gists=>0,
    #   :followers=>9,
    #   :following=>1,
    #   :created_at=>2012-07-21 17:46:24 UTC,
    #   :updated_at=>2016-09-21 12:37:27 UTC
    end
  end

  # It would be nice to replace this with a constructor
  # Unfortunately, my ruby is not great yet.
  def setContext(_context)
    puts "Setting context in handler"
    @context = _context
  end
end
