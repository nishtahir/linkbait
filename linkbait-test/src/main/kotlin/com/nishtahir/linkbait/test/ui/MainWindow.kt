package com.nishtahir.linkbait.test.ui

import com.nishtahir.linkbait.test.controller.Message
import com.nishtahir.linkbait.test.controller.TestBotController
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import tornadofx.*
/**
 * Created by maxke on 22.08.2016.
 * Main GUI
 */
class MainWindow(): View() {
    override val root = BorderPane()
    val controller: TestBotController by inject()
    var tfMessage: TextField by singleAssign()
    var tblMessages: TableView<Message> by singleAssign()
    init {
        with(root) {
            prefHeight = 786.0
            prefWidth = 1024.0
            maxHeight = prefHeight
            maxWidth = prefWidth
            padding = Insets(12.0)
            center {
                vbox {
                    prefHeight = root.prefHeight
                    prefWidth = root.prefWidth
                    borderpaneConstraints { alignment = Pos.CENTER }
                    tblMessages = tableview(controller.msgs) {
                        column("Time", Message::time)
                        column("Sender", Message::sender)
                        column("Message", Message::msg)
                        vboxConstraints {
                            vGrow = Priority.ALWAYS
                        }
                    }
                }
            }
            bottom {
                hbox {
                    prefHeight = 48.0
                    prefWidth = root.prefWidth
                    borderpaneConstraints {
                        marginTop = 12.0
                        alignment = Pos.BOTTOM_CENTER
                    }
                    tfMessage = textfield {
                        id = "tfMessage"
                        hboxConstraints {
                            marginRight = 12.0
                            hGrow = Priority.ALWAYS
                        }
                    }
                    button {
                        id = "btnSend"
                        text = "Send"
                        setOnAction {
                            controller.sendMessage(tfMessage.text)
                        }
                    }
                }
            }
        }
    }
}