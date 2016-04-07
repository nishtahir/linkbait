package com.nishtahir.linkbait.service

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.nishtahir.linkbait.model.Vend

class VendService {
    final String DEFAULT_VEND_LIST_URL = "https://raw.githubusercontent.com/EugeneKay/it-vends/vending/vendlist.php"

    Dao<Vend, String> vendDao
    Random random

    VendService(ConnectionSource connectionSource) {
        vendDao = DaoManager.createDao(connectionSource, Vend.class)

        random = new Random()
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

                    createVend(new Vend(item: item, rarity: rarity, publisher: null))
                }
            }
        }
    }

    def createVend(Vend vend) {
        Vend existingVend = findVendByItem(vend.item)

        if (existingVend) {
            if (vend.rarity == Vend.Rarity.RARE) {
                existingVend.rarity = Vend.Rarity.UNCOMMON
            } else if (vend.rarity == Vend.Rarity.UNCOMMON) {
                existingVend.rarity = Vend.Rarity.COMMON
            }

            updateVend(existingVend)
            return existingVend
        } else {
            return vendDao.createIfNotExists(vend)
        }
    }

    def updateVend(Vend vend) {
        vendDao.update(vend)
    }

    Vend findRandomVend() {
        // It's time to vend, we need something
        // Let's say there's a 5 in 10 chance of a common vend, 3 in 10 of uncommon and 2 in 10 of rare
        Vend.Rarity rarity

        int chance = random.nextInt(10 - 1) + 1

        if (chance < 6) {
            rarity = Vend.Rarity.COMMON
        } else if (chance < 8) {
            rarity = Vend.Rarity.UNCOMMON
        } else {
            rarity = Vend.Rarity.RARE
        }

        List<Vend> vends = findVendsByRarity(rarity)
        return vends.get(random.nextInt(vends.size()))
    }

    List<Vend> findVends() {
        return vendDao.queryForAll()
    }

    Vend findVendByItem(String item) {
        def query = vendDao.queryBuilder()
                .where()
                .eq("item", item)
                .prepare()
        List<Vend> vends = vendDao.query(query)

        if (vends.size() > 0) {
            return vends.get(0)
        } else {
            return null
        }
    }

    List<Vend> findVendsByRarity(Vend.Rarity rarity) {
        def query = vendDao.queryBuilder()
                .where()
                .eq("rarity", rarity)
                .prepare()
        return vendDao.query(query)
    }
}
