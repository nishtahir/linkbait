package com.nishtahir.linkbait

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource

/**
 * This is a pokemon center.
 */
class PokemonService {

    Dao<Pokemon, Double> pokemonDao

    PokemonService(ConnectionSource connectionSource) {
        pokemonDao = DaoManager.createDao(connectionSource, Pokemon.class)
    }

    /**
     * Returns pokemon with matching name
     * @param name
     * @return
     */
    public Pokemon findPokemon(String name){
        def builder = pokemonDao.queryBuilder()
        def query = builder.where()
                .eq("name", name.toLowerCase())
                .prepare()
        return pokemonDao.queryForFirst(query)
    }
}
