package com.nishtahir.linkbait.test

import com.nishtahir.linkbait.test.controller.TestBotController
import com.nishtahir.linkbait.test.ui.MainWindow
import javafx.stage.Stage
import tornadofx.App

/**
 * Created by maxke on 22.08.2016.
 * Test GUI main entry
 */
class TestBotApplication : App() {

    override val primaryView = MainWindow::class
    val mainController: TestBotController by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        mainController.init()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(TestBotApplication::class.java)
        }
    }
}