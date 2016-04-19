package com.nishtahir.linkbait.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical

/**
 * Model describing a Vend.
 */
@Canonical
@DatabaseTable
class Vend {

    /**
     * Rarity of an item vaguely describes how often it should be vended.
     */
    enum Rarity {
        COMMON(5),
        UNCOMMON(3),
        RARE(2)

        /**
         * Scale of 1(most rare) - 10(least rare). How rare a given vend is.
         */
        int index

        Rarity(int index){
            this.index = index
        }

        int getIndex(){
            return index;
        }
    }

    @DatabaseField(generatedId = true)
    int id

    @DatabaseField(unique = true)
    String item

    @DatabaseField
    Rarity rarity

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    User publisher

    /**
     * Promote the rarity of vend
     */
    public void promote(){
        switch (rarity){
            case Rarity.RARE:
                rarity = Rarity.UNCOMMON
                break
            case Rarity.UNCOMMON:
                rarity = Rarity.COMMON
                break
                default:
                    return
        }
    }
}
