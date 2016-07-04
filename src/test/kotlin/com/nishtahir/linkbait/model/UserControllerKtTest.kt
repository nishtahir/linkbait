package com.nishtahir.linkbait.model

import com.j256.ormlite.db.DerbyEmbeddedDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import org.jetbrains.spek.api.Spek
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.addSingleton
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Created by nish on 6/23/16.
 */
class UserControllerKtTest : Spek({
    val testDatabaseURL = "jdbc:derby:data/test;create=true"
    Injekt.addSingleton(JdbcConnectionSource(testDatabaseURL, DerbyEmbeddedDatabaseType()))

    describe("Validate username") {
        it("should return true for valid usernames") {
            assertTrue(isValidUsername("xXx360NoScopexXx"))
        }

        it("Should not be less than 3 characters"){
            assertFalse(isValidUsername("no"))
        }
        it("Should not be more than 20 characters"){
            assertFalse(isValidUsername("ThisUsernameIsCertainlyMoreThanTwentyCharactersAndIsNotOk"))
        }
        it("Empty user names always fail"){
            assertFalse(isValidUsername(""))
        }
    }

    describe("Validate email") {
        it("should return true for valid emails") {
            assertTrue(isValidEmail("test@example.com"))
        }

        it("should return false for invalid emails"){
            assertFalse(isValidUsername("no"))
        }
        it("should return false for empty emails"){
            assertFalse(isValidUsername(""))
        }
    }
})