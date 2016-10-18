package com.nishtahir.linkbait.github;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Created by nish on 10/4/16.
 */
public class GithubHandlerTest {

    @Test
    public void testGithubRepoPatternValidUrl() throws Exception {
        GithubHandler handler = new GithubHandler(null);
        System.out.println(handler.GITHUB_REPO_PATTERN().toString());
        Matcher matcher = handler.GITHUB_REPO_PATTERN().compile().matcher("https://github.com/Kotlin/anko/");
        if(matcher.find()){
            String user = matcher.group(5);
            String repo = matcher.group(9);

            assertEquals(user, "Kotlin");
            assertEquals(repo, "anko");
        } else {
            fail();
        }
    }

    @Test
    public void testGithubRepoPatternInvalidUrl() throws Exception {
        GithubHandler handler = new GithubHandler(null);
        System.out.println(handler.GITHUB_REPO_PATTERN().toString());
        Matcher matcher = handler.GITHUB_REPO_PATTERN().compile().matcher("https://github.com/Kotlin/anko/issues");
        assertFalse(matcher.find());
    }


    @Test
    public void testGithubProfilePatternValidUrl() throws Exception {
        GithubHandler handler = new GithubHandler(null);
        System.out.println(handler.GITHUB_PROFILE_PATTERN().toString());
        Matcher matcher = handler.GITHUB_PROFILE_PATTERN().compile().matcher("https://github.com/Kotlin/");
        if(matcher.find()){
            String user = matcher.group(5);

            assertEquals(user, "Kotlin");
        } else {
            fail();
        }
    }

    @Test
    public void testGithubProfilePatternInvalidUrl() throws Exception {
        GithubHandler handler = new GithubHandler(null);
        System.out.println(handler.GITHUB_PROFILE_PATTERN().toString());
        Matcher matcher = handler.GITHUB_PROFILE_PATTERN().compile().matcher("https://github.com/Kotlin/anko");
        assertFalse(matcher.find());
    }

}