package com.nishtahir.linkbait.model

val OK = 200

val BAD_REQUEST = 400
val INTERNAL_SERVER_ERROR = 500

val INVALID_EMAIL = 1001
val INVALID_USERNAME = 1002
val INVALID_PASSWORD = 1003

val EMAIL_IN_USE = 1011
val USERNAME_IN_USE = 1012

val OTHER_ERROR = 1000

data class ServerError(val code : Int, val message: String, val description: String)