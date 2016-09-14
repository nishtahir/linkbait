package com.nishtahir.linkbait.linklogger;

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for the URL regex.
 * Inspired by https://mathiasbynens.be/demo/url-regex
 *
 * For some reason, gradle still tries to run
 * specifications that have been marked with x-spec.
 * So I had no choice but to comment them out.
 */
class UrlMatcherSpecification : Spek ({

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:test.sqlite"))
    InjektModule.scope.addFactory { LinkLoggerService() }


    val mockConfig = MockConfiguration()
    val mockMessenger = MockMessenger()
    val mockContext = MockContext(mockConfig, mockMessenger)

    val handler: LinkLoggerHandler = LinkLoggerHandler(mockContext)

    describe("urls that should match") {

        it("should match http://foo.com/blah_blah") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_blah"))
        }

        it("should match http://foo.com/blah_blah/") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_blah/"))
        }

        it("should match http://foo.com/blah_blah_(wikipedia)") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_blah_(wikipedia)"))
        }

        it("should match http://foo.com/blah_blah_(wikipedia)_(again)") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_blah_(wikipedia)_(again)"))
        }

        it("should match http://www.example.com/wpstyle/?p=364") {
            assertTrue(handler.URL_PATTERN.matches("http://www.example.com/wpstyle/?p=364"))
        }

        it("should match https://www.example.com/foo/?bar=baz&inga=42&quux") {
            assertTrue(handler.URL_PATTERN.matches("https://www.example.com/foo/?bar=baz&inga=42&quux"))
        }

        it("should match http://userid:password@example.com:8080") {
            assertTrue(handler.URL_PATTERN.matches("http://userid:password@example.com:8080"))
        }

        it("should match http://userid:password@example.com:8080/") {
            assertTrue(handler.URL_PATTERN.matches("http://userid:password@example.com:8080/"))
        }

        it("should match http://userid@example.com") {
            assertTrue(handler.URL_PATTERN.matches("http://userid@example.com"))
        }

        it("should match http://userid@example.com/") {
            assertTrue(handler.URL_PATTERN.matches("http://userid@example.com/"))
        }

        it("should match http://userid@example.com:8080") {
            assertTrue(handler.URL_PATTERN.matches("http://userid@example.com:8080"))
        }

        it("should match http://userid@example.com:8080/") {
            assertTrue(handler.URL_PATTERN.matches("http://userid@example.com:8080/"))
        }

        it("should match http://userid:password@example.com") {
            assertTrue(handler.URL_PATTERN.matches("http://userid:password@example.com"))
        }

        it("should match http://userid:password@example.com/") {
            assertTrue(handler.URL_PATTERN.matches("http://userid:password@example.com/"))
        }

        it("should match http://foo.com/blah_(wikipedia)#cite-1") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_(wikipedia)#cite-1"))
        }

        it("should match http://foo.com/blah_(wikipedia)_blah#cite-1") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/blah_(wikipedia)_blah#cite-1"))
        }

        it("should match http://foo.com/(something)?after=parens") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.com/(something)?after=parens"))
        }

        it("should match http://code.google.com/events/#&product=browser") {
            assertTrue(handler.URL_PATTERN.matches("http://code.google.com/events/#&product=browser"))
        }

        it("should match http://j.mp") {
            assertTrue(handler.URL_PATTERN.matches("http://j.mp"))
        }

        it("should match ftp://foo.bar/baz") {
            assertTrue(handler.URL_PATTERN.matches("ftp://foo.bar/baz"))
        }

        it("should match http://foo.bar/?q=Test%20URL-encoded%20stuff") {
            assertTrue(handler.URL_PATTERN.matches("http://foo.bar/?q=Test%20URL-encoded%20stuff"))
        }

        it("should match http://1337.net") {
            assertTrue(handler.URL_PATTERN.matches("http://1337.net"))
        }

        it("should match http://a.b-c.de") {
            assertTrue(handler.URL_PATTERN.matches("http://a.b-c.de"))
        }

//        Below this line lies tests that would be nice to pass. Edge cases that are unlikely,
//        but will probably happen.
//
//        xit("should match http://142.42.1.1/") {
//            assertTrue(handler.URL_PATTERN.matches("http://142.42.1.1/"))
//        }
//
//        xit("should match http://142.42.1.1:8080/") {
//            assertTrue(handler.URL_PATTERN.matches("http://142.42.1.1:8080/"))
//        }
//
//        xit("should match http://➡.ws/䨹") {
//            assertTrue(handler.URL_PATTERN.matches("http://➡.ws/䨹"))
//        }
//
//        xit("should match http://⌘.ws") {
//            assertTrue(handler.URL_PATTERN.matches("http://⌘.ws"))
//        }
//
//        xit("should match http://⌘.ws/") {
//            assertTrue(handler.URL_PATTERN.matches("http://⌘.ws/"))
//        }
//
//        xit("should match http://مثال.إختبار") {
//            assertTrue(handler.URL_PATTERN.matches("http://مثال.إختبار"))
//        }
//
//        xit("should match http://例子.测试") {
//            assertTrue(handler.URL_PATTERN.matches("http://例子.测试"))
//        }
//
//        xit("should match http://उदाहरण.परीक्षा") {
//            assertTrue(handler.URL_PATTERN.matches("http://उदाहरण.परीक्षा"))
//        }
//
//        xit("should match http://-.~_!$&'()*+,;=:%40:80%2f::::::@example.com") {
//            assertTrue(handler.URL_PATTERN.matches("http://-.~_!$&'()*+,;=:%40:80%2f::::::@example.com"))
//        }
//
//        xit("should match http://223.255.255.254") {
//            assertTrue(handler.URL_PATTERN.matches("http://223.255.255.254"))
//        }
//
//        xit("should match http://☺.damowmow.com/") {
//            assertTrue(handler.URL_PATTERN.matches("http://☺.damowmow.com/"))
//        }
//
//        xit("should match http://✪df.ws/123") {
//            assertTrue(handler.URL_PATTERN.matches("http://✪df.ws/123"))
//        }
//
//        xit("should match http://foo.com/unicode_(✪)_in_parens") {
//            assertTrue(handler.URL_PATTERN.matches("http://foo.com/unicode_(✪)_in_parens"))
//        }

    }

    describe("urls that should not match") {
        it("should not match http://") {
            assertFalse(handler.URL_PATTERN.matches("http://"))
        }

        it("should not match http://.") {
            assertFalse(handler.URL_PATTERN.matches("http://."))
        }

        it("should not match http://..") {
            assertFalse(handler.URL_PATTERN.matches("http://.."))
        }

        it("should not match http://../") {
            assertFalse(handler.URL_PATTERN.matches("http://../"))
        }

        it("should not match http://?") {
            assertFalse(handler.URL_PATTERN.matches("http://?"))
        }

        it("should not match http://??") {
            assertFalse(handler.URL_PATTERN.matches("http://??"))
        }

        it("should not match http://??/") {
            assertFalse(handler.URL_PATTERN.matches("http://??/"))
        }

        it("should not match http://#") {
            assertFalse(handler.URL_PATTERN.matches("http://#"))
        }

        it("should not match http://##") {
            assertFalse(handler.URL_PATTERN.matches("http://##"))
        }

        it("should not match http://##/") {
            assertFalse(handler.URL_PATTERN.matches("http://##/"))
        }

        it("should not match http://foo.bar?q=Spaces should be encoded") {
            assertFalse(handler.URL_PATTERN.matches("http://foo.bar?q=Spaces should be encoded"))
        }

        it("should not match //") {
            assertFalse(handler.URL_PATTERN.matches("//"))
        }

        it("should not match //a") {
            assertFalse(handler.URL_PATTERN.matches("//a"))
        }

        it("should not match ///a") {
            assertFalse(handler.URL_PATTERN.matches("///a"))
        }

        it("should not match ///") {
            assertFalse(handler.URL_PATTERN.matches("///"))
        }

        it("should not match http:///a") {
            assertFalse(handler.URL_PATTERN.matches("http:///a"))
        }

        it("should not match rdar://1234") {
            assertFalse(handler.URL_PATTERN.matches("rdar://1234"))
        }

        it("should not match h://test") {
            assertFalse(handler.URL_PATTERN.matches("h://test"))
        }

        it("should not match http:// shouldfail.com") {
            assertFalse(handler.URL_PATTERN.matches("http:// shouldfail.com"))
        }

        it("should not match :// should fail") {
            assertFalse(handler.URL_PATTERN.matches(":// should fail"))
        }

        it("should not match http://foo.bar/foo(bar)baz quux") {
            assertFalse(handler.URL_PATTERN.matches("http://foo.bar/foo(bar)baz quux"))
        }

        it("should not match ftps://foo.bar/") {
            assertFalse(handler.URL_PATTERN.matches("ftps://foo.bar/"))
        }

        it("should not match http://0.0.0.0") {
            assertFalse(handler.URL_PATTERN.matches("http://0.0.0.0"))
        }

        it("should not match http://10.1.1.0") {
            assertFalse(handler.URL_PATTERN.matches("http://10.1.1.0"))
        }

        it("should not match http://10.1.1.255") {
            assertFalse(handler.URL_PATTERN.matches("http://10.1.1.255"))
        }

        it("should not match http://224.1.1.1") {
            assertFalse(handler.URL_PATTERN.matches("http://224.1.1.1"))
        }

        it("should not match http://1.1.1.1.1") {
            assertFalse(handler.URL_PATTERN.matches("http://1.1.1.1.1"))
        }

        it("should not match http://123.123.123") {
            assertFalse(handler.URL_PATTERN.matches("http://123.123.123"))
        }

        it("should not match http://3628126748") {
            assertFalse(handler.URL_PATTERN.matches("http://3628126748"))
        }

        it("should not match http://10.1.1.1") {
            assertFalse(handler.URL_PATTERN.matches("http://10.1.1.1"))
        }

        it("should not match foo.com") {
            assertFalse(handler.URL_PATTERN.matches("foo.com"))
        }

//        xit("should not match http://.www.foo.bar/") {
//            assertFalse(handler.URL_PATTERN.matches("http://.www.foo.bar/"))
//        }
//
//        xit("should not match http://www.foo.bar./") {
//            assertFalse(handler.URL_PATTERN.matches("http://www.foo.bar./"))
//        }
//
//        xit("should not match http://.www.foo.bar./") {
//            assertFalse(handler.URL_PATTERN.matches("http://.www.foo.bar./"))
//        }
//
//        xit("should not match http://-error-.invalid/") {
//            assertFalse(handler.URL_PATTERN.matches("http://-error-.invalid/"))
//        }
//
//        xit("should not match http://a.b--c.de/") {
//            assertFalse(handler.URL_PATTERN.matches("http://a.b--c.de/"))
//        }
//
//        xit("should not match http://-a.b.co") {
//            assertFalse(handler.URL_PATTERN.matches("http://-a.b.co"))
//        }
//
//        xit("should not match http://a.b-.co") {
//            assertFalse(handler.URL_PATTERN.matches("http://a.b-.co"))
//        }

    }
})