package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JobSearchHandlerSpec : Spek({

    val TRIGGER = "jobs"

    val PATTERN = """^$TRIGGER\s?+((\w|\s)+)(\s?+,\s?+)((\w|\s)+)$""".toRegex()

    val HELP_MESSAGE = """
```
Usage:
$TRIGGER [tags], [location]
```
Searches LinkedIn and returns new job postings

"""


    val mockConfig = MockConfiguration()
    val mockMessenger = MockMessenger()
    val mockContext = MockContext(mockConfig, mockMessenger)

    val handler = JobSearchHandler(mockContext)

    describe("a message event ") {
        given("a simple message") {
            val messageEvent: MessageEvent = MessageEvent()
            messageEvent.isDirectedAtBot = true
            messageEvent.channel = "test"
            messageEvent.message = "$TRIGGER"

            it("should send help message") {
                handler.handleMessageEvent(messageEvent)
                assertTrue(mockMessenger.message == HELP_MESSAGE)
            }
        }

        given("a message with missing location") {
            it("should send help message") {
                val messageEvent: MessageEvent = MessageEvent()
                messageEvent.isDirectedAtBot = true
                messageEvent.channel = "test"
                messageEvent.message = "$TRIGGER android"

                handler.handleMessageEvent(messageEvent)
                assertTrue(mockMessenger.message == HELP_MESSAGE)
            }
        }

        given("pattern constant") {
            it("should not missing tags and location") {
                assertFalse(PATTERN.matches("$TRIGGER"))
            }

            it("should not match missing tags") {
                assertFalse(PATTERN.matches("$TRIGGER, Seattle WA"))
            }

            it("should not match missing location") {
                assertFalse(PATTERN.matches("$TRIGGER android"))
            }

            it("should match valid pattern") {
                assertTrue(PATTERN.matches("$TRIGGER android,Seattle WA"))
                assertTrue(PATTERN.matches("$TRIGGER android,Seattle WA"))
                assertTrue(PATTERN.matches("$TRIGGER android development,Seattle WA"))
                assertTrue(PATTERN.matches("$TRIGGER android development ,Seattle WA"))
            }
        }

        given("a message") {
            it("should return a valid job req") {
                val tag1 = "android"
                val tag2 = "development"
                val location = "Seattle WA"
                val req = handler.parseJobRequest("$TRIGGER $tag1 $tag2 ,$location")

                assertTrue(req.tags.size == 2)
                assertEquals(tag1, req.tags[0], "first tag is wrong")
                assertEquals(tag2, req.tags[1], "second tag is wrong")
                assertEquals(location, req.location, "location is wrong")
            }
        }

    }
})
