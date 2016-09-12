package com.nishtahir.linkbait.extensions;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeoutException

fun <T> logger(cls: Class<T>): Logger {
    return LoggerFactory.getLogger(cls.simpleName)
}

fun Logger.i(message: String) {
    this.info(message)
}

fun Logger.d(message: String) {
    this.debug(message)
}

fun Logger.e(message: String) {
    this.error(message)
}

fun Logger.e(message: String, exception: Exception) {
    this.error(message, exception)
}