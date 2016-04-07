package com.nishtahir.linkbait.service

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.nishtahir.linkbait.model.User
import com.nishtahir.linkbait.model.Vend
import spock.lang.Specification

class VendServiceTest extends Specification {
    ConnectionSource connectionSource
    User localUser
    UserService userService
    VendService vendService

    void setup() {
        Class.forName("org.sqlite.JDBC")
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:linkbait-test.sqlite")
        TableUtils.createTableIfNotExists(connectionSource, User.class)
        TableUtils.createTableIfNotExists(connectionSource, Vend.class)

        userService = new UserService(connectionSource)
        localUser = userService.createUser(new User(slackUserId: "1234", username:"nish", upvotes:10, downvotes:5))

        vendService = new VendService(connectionSource, false)
    }

    void cleanup() {
        TableUtils.clearTable(connectionSource, Vend.class)
        TableUtils.clearTable(connectionSource, User.class)
        connectionSource.close()
    }

    def "checkDefaultList_WithNoDefaultList_GetsDefaultList"() {
        when:
        vendService.checkDefaultList()

        then:
        vendService.findVends().size() > 0
    }

    def "createVend_WithNewVend_CreatesVend"() {
        given:
        String item = "a bag of M&Ms"
        Vend localVend = new Vend(item: item, rarity: Vend.Rarity.UNCOMMON, publisher: localUser)

        when:
        vendService.createVend(localVend)

        then:
        Vend foundVend = vendService.findVendByItem(item)
        foundVend != null
        foundVend == localVend
    }

    def "createVend_WithExistingVend_UpdatesVend"() {
        given:
        String item = "a hot pocket"
        Vend localVend = new Vend(item: item, rarity: Vend.Rarity.UNCOMMON, publisher: localUser)
        vendService.createVend(localVend)

        when:
        vendService.createVend(new Vend(item: item, rarity: Vend.Rarity.UNCOMMON, publisher: localUser))

        then: "there should only be one common vend since the existing one was updated"
        List<Vend> allVends = vendService.findVends()
        allVends != null
        allVends.size() == 1
        allVends.get(0).rarity == Vend.Rarity.COMMON
    }

    def "findRandomVend_FindsRandomVend"() {
        given:
        Vend commonVend = new Vend(item: "a Pepsi", rarity: Vend.Rarity.COMMON, publisher: localUser)
        Vend uncommonVend = new Vend(item: "a chillidog", rarity: Vend.Rarity.UNCOMMON, publisher: localUser)
        Vend rareVend = new Vend(item: "another vending machine", rarity: Vend.Rarity.RARE, publisher: localUser)
        vendService.createVend(commonVend)
        vendService.createVend(uncommonVend)
        vendService.createVend(rareVend)

        expect:
        for (int _ = 0; _ < 10; _++) {
            vendService.findRandomVend() != null
        }
    }

    def "findVendByItem_WithExistingVend_FindsVend"() {
        given:
        String item = "1400 ladybugs"
        Vend localVend = new Vend(item: item, rarity:  Vend.Rarity.RARE, publisher:  localUser)
        vendService.createVend(localVend)

        expect:
        Vend databaseVend = vendService.findVendByItem(item)
        databaseVend != null
        databaseVend == localVend
    }

    def "findVendsByRarity_WithExistingVends_FindsVends"() {
        given:
        String item1 = "a gold ingot"
        String item2 = "a cupcake"
        String item3 = "a small puppy"
        Vend vend1 = new Vend(item: item1, rarity:  Vend.Rarity.UNCOMMON, publisher:  localUser)
        Vend vend2 = new Vend(item: item2, rarity:  Vend.Rarity.UNCOMMON, publisher:  localUser)
        Vend vend3 = new Vend(item:  item3, rarity:  Vend.Rarity.RARE, publisher:  localUser)
        vendService.createVend(vend1)
        vendService.createVend(vend2)
        vendService.createVend(vend3)

        expect:
        List<Vend> uncommonVends = vendService.findVendsByRarity(Vend.Rarity.UNCOMMON)
        uncommonVends != null
        uncommonVends.size() == 2
        uncommonVends.containsAll([vend1, vend2])
        !uncommonVends.contains(vend3)
    }

    def "updateVend_WithExistingVend_UpdatesVend"() {
        given:
        String item = "bees?!?"
        Vend localVend = new Vend(item: item, rarity:  Vend.Rarity.RARE, publisher:  localUser)
        vendService.createVend(localVend)

        when:
        localVend.rarity = Vend.Rarity.UNCOMMON
        vendService.updateVend(localVend)

        then:
        Vend databaseVend = vendService.findVendByItem(item)
        databaseVend != null
        databaseVend == localVend
    }
}
