package com.nishtahir.linkbait.commons
import org.openqa.selenium.WebDriver

interface BrowserAction {
    fun doOnExecutorPool(driver: WebDriver)
}