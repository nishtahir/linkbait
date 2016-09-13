package com.nishtahir.linkbait.linklogger

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.test.MockConfiguration
import com.nishtahir.linkbait.test.MockContext
import com.nishtahir.linkbait.test.MockMessenger
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class LinkLoggerHandlerSpecification : Spek ({

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:test.sqlite"))
    InjektModule.scope.addFactory { LinkLoggerService() }

    val mockConfig = MockConfiguration()
    val mockMessenger = MockMessenger()
    val mockContext = MockContext(mockConfig, mockMessenger)

    val handler: LinkLoggerHandler = LinkLoggerHandler(mockContext)

    describe("TODO") {

    }
})