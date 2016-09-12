package com.nishtahir.linkbait.linklogger

import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class LinkLoggerHandlerSpecification : Spek ({

    val mockConfig = MockConfiguration()
    val mockMessenger = MockMessenger()
    val mockContext = MockContext(mockConfig, mockMessenger)

    val handler: LinkLoggerHandler = LinkLoggerHandler(mockContext)

    describe("test"){
        it("test"){
            val string = "https://www.google.ee/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiJ9du96orPAhXKA5oKHaqFCkEQjRwIBw&url=https%3A%2F%2Fwww.nasa.gov%2Fmission_pages%2Fnewhorizons%2Fimages%2F&psig=AFQjCNFAGhL2kAOMT8-PUPk1QDe-yW8EKA&ust=1473804160780984 https://wall.alphacoders.com/by_sub_category.php?id=168562"
            assertEquals(handler.getLinksFromString(string), listOf("https://www.google.ee/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiJ9du96orPAhXKA5oKHaqFCkEQjRwIBw&url=https%3A%2F%2Fwww.nasa.gov%2Fmission_pages%2Fnewhorizons%2Fimages%2F&psig=AFQjCNFAGhL2kAOMT8-PUPk1QDe-yW8EKA&ust=1473804160780984",
                    "https://wall.alphacoders.com/by_sub_category.php?id=168562"))
        }
    }
})