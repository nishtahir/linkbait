package com.nishtahir.linkbait.test.controller;

import com.nishtahir.linkbait.test.ui.MainWindow
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.FX
import tornadofx.resizeColumnsToFitContent
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by maxke on 22.08.2016.
 * Main controller, duh
 */
class TestBotController: Controller() {

    val screen: MainWindow by inject()
    val msgs: ObservableList<Message> = FXCollections.observableArrayList<Message>()

    fun init() {
        if (FX.primaryStage.scene.root != screen.root) {
            FX.primaryStage.scene.root = screen.root
            FX.primaryStage.sizeToScene()
            FX.primaryStage.centerOnScreen()
        }

        screen.title = "Linkbait"
    }

    fun sendMessage(text: String) {
        val now = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        msgs.add(Message(now, "user", text))
        screen.tblMessages.resizeColumnsToFitContent()
    }
}

data class Message(
        val time: String,
        val sender: String,
        val msg: String
)