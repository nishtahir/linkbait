package com.nishtahir.linkbait;

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.runtime.Helpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;
import com.nishtahir.linkbait.core.request.RequestHandler;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackEvent;


public class RedditAutoCompleteHandler extends RubyObject implements RequestHandler {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'java'\n" +
            "java_package 'com.nishtahir.linkbait'\n" +
            "java_import 'com.nishtahir.linkbait.core.request.RequestHandler'\n" +
            "java_import 'com.ullink.slack.simpleslackapi.SlackSession'\n" +
            "java_import 'com.ullink.slack.simpleslackapi.events.SlackEvent'\n" +
            "\n" +
            "class RedditAutoCompleteHandler\n" +
            "  java_implements RequestHandler\n" +
            "  SlackSession = com.ullink.slack.simpleslackapi.SlackSession\n" +
            "  SlackMessagePosted = com.ullink.slack.simpleslackapi.events.SlackMessagePosted\n" +
            "\n" +
            "  java_signature 'int sum(int, int)'\n" +
            "  def sum(a,b)\n" +
            "    a + b\n" +
            "  end\n" +
            "\n" +
            "  java_signature 'String parse(String, String)'\n" +
            "  puts 'parsing'\n" +
            "  def parse(message, session_id)\n" +
            "    if match = message.match(/(^|\\s+)\\/?(?<sub>(r\\/\\w+))\\/?(\\s+|$)/)\n" +
            "      return match[:sub]\n" +
            "    end\n" +
            "    puts 'unnable to match'\n" +
            "    return ''\n" +
            "  end\n" +
            "\n" +
            "  java_signature 'boolean handle(SlackSession, SlackEvent)'\n" +
            "  def handle(session, event)\n" +
            "    subreddit = parse(event.getMessageContent(), session.sessionPersona().id)\n" +
            "    puts 'message: #{session.getMessageContent()}'\n" +
            "    if subreddit.to_s == ''\n" +
            "        puts 'subreddit is empty'\n" +
            "        return false\n" +
            "    end\n" +
            "    session.sendMessage(event.getChannel(), \"For the lazy... <https://www.reddit.com/#{subreddit}/|https://www.reddit.com/#{subreddit}/>\", nil)\n" +
            "    return true\n" +
            "  end\n" +
            "\n" +
            "end\n" +
            "").toString();
        __ruby__.executeScript(source, "src/main/ruby/reddit_autocomplete.rb");
        RubyClass metaclass = __ruby__.getClass("RedditAutoCompleteHandler");
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: RedditAutoCompleteHandler");
        metaclass.setRubyStaticAllocator(RedditAutoCompleteHandler.class);
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private RedditAutoCompleteHandler(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new RedditAutoCompleteHandler(ruby, metaClass);
    }

    /**
     * Default constructor. Invokes this(Ruby, RubyClass) with the classloader-static
     * Ruby and RubyClass instances assocated with this class, and then invokes the
     * no-argument 'initialize' method in Ruby.
     */
    public RedditAutoCompleteHandler() {
        this(__ruby__, __metaclass__);
        Helpers.invoke(__ruby__.getCurrentContext(), this, "initialize");
    }


    
    public int sum(int a, int b) {
        IRubyObject ruby_arg_a = JavaUtil.convertJavaToRuby(__ruby__, a);
        IRubyObject ruby_arg_b = JavaUtil.convertJavaToRuby(__ruby__, b);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "sum", ruby_arg_a, ruby_arg_b);
        return (Integer)ruby_result.toJava(int.class);

    }

    
    public String parse(String message, String session_id) {
        IRubyObject ruby_arg_message = JavaUtil.convertJavaToRuby(__ruby__, message);
        IRubyObject ruby_arg_session_id = JavaUtil.convertJavaToRuby(__ruby__, session_id);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "parse", ruby_arg_message, ruby_arg_session_id);
        return (String)ruby_result.toJava(String.class);

    }

    
    public boolean handle(SlackSession session, SlackEvent event) {
        IRubyObject ruby_arg_session = JavaUtil.convertJavaToRuby(__ruby__, session);
        IRubyObject ruby_arg_event = JavaUtil.convertJavaToRuby(__ruby__, event);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "handle", ruby_arg_session, ruby_arg_event);
        return (Boolean)ruby_result.toJava(boolean.class);

    }

}
