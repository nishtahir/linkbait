package com.nishtahir.linkbait.pokedex

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import info.debatty.java.stringsimilarity.JaroWinkler
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.*

val pokemonColorMap = mapOf(
        "black"  to "705746", // black
        "blue"   to "6390F0", // blue
        "brown"  to "E2BF65", // brown
        "gray"   to "B7B7CE", // gray
        "green"  to "7AC74C", // green
        "pink"   to "D685AD", // pink
        "purple" to "A98FF3", // purple
        "red"    to "C22E28", // red
        "white"  to "FFFFFF", // white
        "yellow" to "F7D02C" // yellow
)

val connectionSource: JdbcConnectionSource = Injekt.get()

val pokemonDao: Dao<Pokemon, Double> = DaoManager.createDao(connectionSource, Pokemon::class.java)

fun findPokemon(name: String): Pokemon? {
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

fun findPokemon(id : Int) : Pokemon? {
    val query = pokemonDao.queryBuilder()
            .where()
            .eq("id", id)
            .prepare()
    return pokemonDao.queryForFirst(query)
}

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
 * @param colorId id of color to get. This should match Ids in db
 */
fun getColor(colorId: String = "gray"): String {
    return pokemonColorMap[colorId].orEmpty()
}
