package com.nishtahir.linkbait

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource

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
     * Returns pokemon with matching name
     * @param name
     * @return
     */
    public Pokemon findPokemon(String name) {
        def builder = pokemonDao.queryBuilder()
        def query = builder.where()
                .eq("name", name.toLowerCase())
                .prepare()
        return pokemonDao.queryForFirst(query)
    }
}
