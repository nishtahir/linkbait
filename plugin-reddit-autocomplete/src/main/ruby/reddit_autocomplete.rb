require 'java'
java_package 'com.nishtahir.linkbait'
java_import 'com.nishtahir.linkbait.core.request.RequestHandler'
java_import 'com.ullink.slack.simpleslackapi.SlackSession'
java_import 'com.ullink.slack.simpleslackapi.events.SlackEvent'

class RedditAutoCompleteHandler
  java_implements RequestHandler
  SlackSession = com.ullink.slack.simpleslackapi.SlackSession
  SlackMessagePosted = com.ullink.slack.simpleslackapi.events.SlackMessagePosted

  java_signature 'int sum(int, int)'
  def sum(a,b)
    a + b
  end

  java_signature 'String parse(String, String)'
  puts 'parsing'
  def parse(message, session_id)
    if match = message.match(/(^|\s+)\/?(?<sub>(r\/\w+))\/?(\s+|$)/)
      return match[:sub]
    end
    puts 'unnable to match'
    return ''
  end

  java_signature 'boolean handle(SlackSession, SlackEvent)'
  def handle(session, event)
    subreddit = parse(event.getMessageContent(), session.sessionPersona().id)
    puts 'message: #{session.getMessageContent()}'
    if subreddit.to_s == ''
        puts 'subreddit is empty'
        return false
    end
    session.sendMessage(event.getChannel(), "For the lazy... <https://www.reddit.com/#{subreddit}/|https://www.reddit.com/#{subreddit}/>", nil)
  end

end
