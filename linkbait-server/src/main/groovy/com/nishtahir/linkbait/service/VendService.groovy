package com.nishtahir.linkbait.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.linkbait.model.Vend

import java.util.concurrent.ThreadLocalRandom

class VendService {
    final String DEFAULT_VEND_LIST_URL = "https://raw.githubusercontent.com/EugeneKay/it-vends/vending/vendlist.php"

    /**
     *
     */
    Dao<Vend, String> vendDao

    /**
     * Silly hack to get a list containing enum values corresponding to the rarity of items
     * This allows us to avoid nasty if, else statements later.
     * Instead we can just pick an arbitrary item out of the list
     */
    List<Vend.Rarity> rarityIndexList = []

    /**
     *
     * @param connectionSource
     */
    VendService(ConnectionSource connectionSource) {
        vendDao = DaoManager.createDao(connectionSource, Vend.class)

        Vend.Rarity.values().each { rarity ->
            (0..rarity.index).each {
                rarityIndexList.add(rarity)
            }
        }
    }

    VendService(ConnectionSource connectionSource, boolean checkDefaultList) {
        this(connectionSource)

        if (checkDefaultList) {
            this.checkDefaultList()
        }
    }

    def checkDefaultList() {
        if (findVends().size() == 0) {
            System.out.println("Getting default vend list")
            // We need to get the default list
            def vendList = new URL(DEFAULT_VEND_LIST_URL).getText()

            // Now, this is all nice and PHP formatted, so we have to trundle through it
            vendList.eachLine {
                // https://regex101.com/r/yO2pK8/1
                def matcher = it =~ /^\$(?<type>([^\[]*))\[\]='(?<item>(.*))';$/

                if (matcher.matches()) {
                    String type = matcher.group("type")
                    String item = matcher.group("item").replace("\'", "''")

                    Vend.Rarity rarity
                    if (type.equals("vendspecial")) {
                        rarity = Vend.Rarity.RARE
                    } else {
                        rarity = Vend.Rarity.UNCOMMON
                    }

                    createOrPromoteVend(new Vend(item: item, rarity: rarity, publisher: null))
                }
            }
        }
    }

    /**
     * Creates new vend if it does not already exist
     * @param vend vend to create
     * @return created vend
     */
    Vend createVend(Vend vend) {
        vendDao.createIfNotExists(vend)
    }

    /**
     * Create a new Vend or promote an existing vend rarity
     * @param vend
     */
    def createOrPromoteVend(Vend vend) {
        def existingVend = findVendByItem(vend.item)
        if (existingVend) {
            existingVend.promote()
            updateVend(existingVend)
        } else {
            createVend(vend)
        }
    }

    /**
     * @param vend vend to delete.
     */
    void deleteVend(Vend vend) {
        vendDao.delete(vend)
    }

    /**
     * @param vend vend to update
     */
    void updateVend(Vend vend) {
        vendDao.update(vend)
    }

    /**
     * @return Randomly selected Vend
     */
    Vend getRandomVend() {
        Vend.Rarity rarity = rarityIndexList[ThreadLocalRandom.current().nextInt(rarityIndexList.size())]
        List<Vend> vends = findVendsByRarity(rarity)
        return vends.get(ThreadLocalRandom.current().nextInt(vends.size()))
    }

    /**
     * @return all available vends.
     */
    List<Vend> findVends() {
        return vendDao.queryForAll()
    }

    /**
     * @param item Item to search for.
     * @return first matching Item.
     */
    Vend findVendByItem(String item) {
        return vendDao.queryForFirst(vendDao.queryBuilder()
                .where()
                .eq("item", item)
                .prepare())
    }

    /**
     * @param rarity see {@link Vend.Rarity}
     * @return All vends of a given Rarity.
     */
    List<Vend> findVendsByRarity(Vend.Rarity rarity) {
        def query = vendDao.queryBuilder()
                .where()
                .eq("rarity", rarity)
                .prepare()
        return vendDao.query(query)
    }


}