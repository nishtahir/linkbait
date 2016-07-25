package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.jdbc.JdbcConnectionSource
import org.jetbrains.spek.api.Spek
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PokedexServiceTest : Spek({
    Injekt.addSingleton(JdbcConnectionSource("jdbc:sqlite:src/test/resources/linkbait-pokedex.sqlite"))

    describe("Database connection") {
        it("should not be null") {
            val test: JdbcConnectionSource = Injekt.get()
            assertNotNull(test)
        }
    }

    describe("Pokemon") {
        it("should find exact matches") {
            val test = findPokemon("Jolteon")
            assertEquals("jolteon", test?.name)
            assertEquals("135", test?.id)
        }

        it("should find like matches") {
            val test = findPokemon("Jlten")
            assertEquals("jolteon", test?.name)
            assertEquals("135", test?.id)
        }
    }
})
