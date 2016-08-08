package com.nishtahir.linkbait.cli

import com.beust.jcommander.JCommander
import org.jetbrains.spek.api.Spek
import kotlin.test.assertTrue

/**
 * Created by nish on 7/30/16.
 */
class CommandLinkbaitSpecification : Spek({

    describe("Command line interface ") {

        it("properly sets help option") {
            val linkbaitCmd = CommandLinkbait()
            val input = arrayOf("-h")

            val cmd = JCommander(linkbaitCmd)
            cmd.addCommand(CommandServer())
            cmd.addCommand(CommandBot())

            cmd.parse(*input)
            assertTrue { linkbaitCmd.help!! }

            if(linkbaitCmd.help!!){
                cmd.usage()
            }
        }

    }

})