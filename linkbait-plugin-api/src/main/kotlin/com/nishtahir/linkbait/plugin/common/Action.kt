package com.nishtahir.linkbait.plugin.common
import org.openqa.selenium.WebDriver

interface BrowserAction {
    fun doOnExecutorPool(driver: WebDriver)
}