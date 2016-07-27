package com.nishtahir.linkbait.heysnackfood

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import org.jetbrains.spek.api.Spek
import javax.swing.plaf.TableUI
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by nish on 7/27/16.
 */
class HeySnackFoodServiceSpec : Spek ({

    InjektModule.scope.addSingleton(JdbcConnectionSource("jdbc:sqlite:test.sqlite"))


    val service = HeySnackFoodService()

    describe("Service crud") {

        beforeEach {
            TableUtils.clearTable(InjektModule.scope.get<JdbcConnectionSource>(), User::class.java)
        }

        it ("should insert users") {
            service.createUser("test")
            assertEquals("test", service.findOrCreateUser("test").name)
        }

        it ("should update user") {
            service.createUser("test")
            val user = service.findOrCreateUser("test")
            assertEquals("test", user.name)

            user.count += 1
            service.updateUser(user)

            assertEquals(1, service.findOrCreateUser("test").count)
        }

        it ("should return null if user does not exist") {
            val user: User? = service.findOrCreateUser("test")
            assertNotNull(user)
        }
    }

})