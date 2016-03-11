package com.nishtahir.linkbait.controller

import com.nishtahir.linkbait.service.UserService
import com.nishtahir.linkbait.util.JSONUtils
import groovy.transform.Canonical

import static spark.Spark.get

/**
 * Created by nish on 2/26/16.
 */
@Canonical
class UserController extends AbstractController {

    UserService service

    @Override
    void init() {
        /**
         *  Returns JSON with List of links posted today.
         */
        get("/users", { request, response ->
            response.type("application/json")

            String username = request.queryParams("name");
            if (username != null) {
                return JSONUtils.dataToPrettyJSON(service.findUserByName(username))
            } else {
                return JSONUtils.dataToPrettyJSON(service.users)
            }
        });
    }

}