package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import info.debatty.java.stringsimilarity.JaroWinkler
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.*

class PokedexService {
    val pokemonColorMap = mapOf(
            "black" to "705746", // black
            "blue" to "6390F0", // blue
            "brown" to "E2BF65", // brown
            "gray" to "B7B7CE", // gray
            "green" to "7AC74C", // green
            "pink" to "D685AD", // pink
            "purple" to "A98FF3", // purple
            "red" to "C22E28", // red
            "white" to "FFFFFF", // white
            "yellow" to "F7D02C" // yellow
    )


    /**
     * MissingNo stats gotten from
     * http://bulbapedia.bulbagarden.net/wiki/MissingNo.
     */
    val missingNo: Pokemon = Pokemon(
            id = "????",
            name = "Mi̥̮̖ss̹̺̘͉i͉̭̦̹̙n̘̟̝̦͎g̦͕̠ͅn̟̭o̤̲̮̹̞͔̰",
            weight = 35070,
            height = 100,
            type = "Bird, Normal",
            color = "purple",
            thumbnail = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/MissingNo.png/150px-MissingNo.png",
            description = "31, 32, 50, 52, 56, 61, 62, 63, 67, 68, 69, 79, 80, 81, 86, 87, 94, 95, 115, 121, 122, 127, 134, 135, 137, 140, 146, 156, 159, 160, 161, 162, 172, 174, 175 or 181."
    )

    val connectionSource: JdbcConnectionSource = Injekt.get()

    val pokemonDao: Dao<Pokemon, Double> = DaoManager.createDao(connectionSource, Pokemon::class.java)

    fun findPokemonByName(name: String): Pokemon? {
        val query = pokemonDao.queryBuilder().where()
                .eq("name", name.toLowerCase())
                .prepare()
        val exactMatch = pokemonDao.queryForFirst(query)

        if (exactMatch == null) {
            val likeMatch = findPokemonLike(name)
            if (likeMatch == null) {
                return findPokemonFuzzy(name)
            } else {
                return likeMatch
            }
        } else {
            return exactMatch
        }
    }

    fun findPokemon(obj: Any): Pokemon? {
        when (obj) {
            is Int -> return findPokemonById(obj)
            is String -> return findPokemonByName(obj)
            else -> return null
        }
    }

    /**
     * @param id ID of Pokemon to find.
     * @return Pokemon with matching ID or MissingNo if not found.
     */
    fun findPokemonById(id: Int): Pokemon {
        val query = pokemonDao.queryBuilder()
                .where()
                .eq("id", id)
                .prepare()
        return pokemonDao.queryForFirst(query) ?: missingNo
    }

    /**
     * Fuzzy match pokemon search
     * @param name Name of pokemon to find
     */
    fun findPokemonLike(name: String): Pokemon? {
        val query = pokemonDao.queryBuilder()
                .where()
                .like("name", "%${name.toLowerCase()}%")
                .prepare()
        return pokemonDao.queryForFirst(query)
    }

    /**
     * @param name Name of pokemon
     */
    fun findPokemonFuzzy(name: String): Pokemon? {
        val allPokemon = pokemonDao.queryForAll()

        // Compute the Jaro-Wrinkler-Distance of all Pokemon to our input
        // While this might be slow for large data sets it runs about 1-3 ms on my machine
        val jaroWrinkler = JaroWinkler()
        val distances = TreeMap<Double, Pokemon>(Collections.reverseOrder()) // Saves us from sorting
        for (pokemnon: Pokemon in allPokemon) {
            distances.put(jaroWrinkler.similarity(pokemnon.name, name), pokemnon)
        }

        if (distances.size == 0) {
            return null
        } else {
            return distances.values.first()
        }
    }

    /**
     * @param color Color to get.
     * @return Hex matching pokemon color
     */
    fun getColorHex(color: String = "gray"): String {
        return pokemonColorMap[color].orEmpty()
    }
}