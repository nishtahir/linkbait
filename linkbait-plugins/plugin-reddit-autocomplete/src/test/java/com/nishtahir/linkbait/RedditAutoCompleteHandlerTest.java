package com.nishtahir.linkbait;

import org.junit.Test;

/**
 * Created by nish on 4/23/16.
 */
public class RedditAutoCompleteHandlerTest {

    @Test
    public void testParse() throws Exception {
        RedditAutoCompleteHandler handler = new RedditAutoCompleteHandler();
        handler.parse("/r/Android", "bot");
    }
}