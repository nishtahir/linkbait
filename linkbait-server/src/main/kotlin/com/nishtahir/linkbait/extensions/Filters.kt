package com.nishtahir.linkbait.extensions

import com.nishtahir.linkbait.model.User
import spark.Request
import spark.Response

/**
 * Redirects the route to route with trailing slash
 */
val addTrailingSlashes = { request: Request, response: Response ->
    if (!request.pathInfo().endsWith("/")) {
        response.redirect(request.pathInfo() + "/")
    }
}

/**
 * @return Username from given request. Empty if unavailable
 */
fun getUsernameFromQuery(request: Request): String {
    return request.queryParams(User::name.name).orEmpty()
}

/**
 * @return Email from given request. Empty if unavailable
 */
fun getEmailFromQuery(request: Request): String {
    return request.queryParams(User::email.name).orEmpty()
}

/**
 * @return Password from given request. Empty if unavailable
 */
fun getPasswordFromQuery(request: Request): String {
    return request.queryParams(User::password.name).orEmpty()
}