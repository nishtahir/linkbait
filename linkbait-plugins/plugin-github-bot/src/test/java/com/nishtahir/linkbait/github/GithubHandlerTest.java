package com.nishtahir.linkbait.github;

import com.nishtahir.linkbait.plugin.MessageEvent;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Created by nish on 10/4/16.
 */
public class GithubHandlerTest {
    @Test
    public void GITHUB_PATTERN() throws Exception {
        GithubHandler handler = new GithubHandler(null);
        System.out.println(handler.GITHUB_PATTERN().toString());
        Matcher matcher = handler.GITHUB_PATTERN().compile().matcher("https://github.com/Kotlin/anko/");
        if(matcher.find()){
            String user = matcher.group(5);
            String repo = matcher.group(9);

            assertEquals(user, "Kotlin");
            assertEquals(repo, "anko");
        } else {
            fail();
        }

//        assert(matcher.matches());
//        for(int i = 0; i < matcher.groupCount(); i++){
//            System.out.println("" + i + ": " + matcher.group(i));
//        }
//        System.out.println(groups);
    }

    @Test
    public void testHandleMessageEvent(){
//        MessageEvent mockEvent = new MessageEvent();
//        mockEvent.setMessage("https://github.com/Kotlin/anko");
//        GithubHandler handler = new GithubHandler(null);
//        handler.handleMessageEvent(mockEvent);
    }

}