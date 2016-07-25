package com.nishtahir.linkbait

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import info.debatty.java.stringsimilarity.JaroWinkler

/**
 * This is a pokemon center.
 */
class PokemonService {

    def pokemonColorMap = [
            1: '705746', // black
            2: '6390F0', // blue
            3: 'E2BF65', // brown
            4: 'B7B7CE', // gray
            5: '7AC74C', // green
            6: 'D685AD', // pink
            7: 'A98FF3', // purple
            8: 'C22E28', // red
            9: 'FFFFFF', // white
            10: 'F7D02C', // yellow
    ]

    /**
     *
     */
    Dao<Pokemon, Double> pokemonDao

    /**
     *
     * @param connectionSource
     */
    PokemonService(ConnectionSource connectionSource) {
        pokemonDao = DaoManager.createDao(connectionSource, Pokemon.class)
    }

    /**
     * Returns pokemon with matching name. Matching happens in 3 steps
     * 1. Exact match:  Returns a pokemeon when its name matches the input exactly
     * 2. Like match:   Matches *input*. Useful for prefix or suffix inputs
     * 3. Fuzzy match:  Returns the pokemon whose name is most similar to the input
     *                  Similarity is defined by the Jaro-Wrinkler distance of name and the input.
     * @param name
     * @return
     */
    public Pokemon findPokemon(String name) {
        def builder = pokemonDao.queryBuilder()
        def query = builder.where()
                .eq("name", name.toLowerCase())
                .prepare()
        def exactMatch = pokemonDao.queryForFirst(query)

        if (exactMatch == null) {
            def likeMatch = findPokemonLike(name)
            if (likeMatch == null) {
                return findPokemonFuzzy(name)
            } else {
                return likeMatch
            }
        } else {
            return exactMatch
        }
    }

    Pokemon findPokemonLike(String name) {
        def query = pokemonDao.queryBuilder()
                .where()
                .like("name", "%${name.toLowerCase()}%")
                .prepare()
        return pokemonDao.queryForFirst(query)
    }

    Pokemon findPokemonFuzzy(String name) {
        def allPokemon = pokemonDao.queryForAll()

        // Compute the Jaro-Wrinkler-Distance of all Pokemon to our input
        // While this might be slow for large data sets it runs about 1-3 ms on my machine
        def jaroWrinkler = new JaroWinkler()
        def distances = new TreeMap(Collections.reverseOrder()) // Saves us from sorting
        for (Pokemon pokemnon: allPokemon) {
            distances.put(jaroWrinkler.similarity(pokemnon.name, name), pokemnon)
        }

        if (distances.size() == 0) {
            return null
        } else {
            return distances.values().first() as Pokemon
        }
    }
}
