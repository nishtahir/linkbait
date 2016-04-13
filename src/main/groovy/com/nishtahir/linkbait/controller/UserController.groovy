package com.nishtahir.linkbait.controller

import com.nishtahir.linkbait.service.UserService
import com.nishtahir.linkbait.util.JsonUtils
import groovy.transform.Canonical

import static spark.Spark.get

/**
 * Created by nish on 2/26/16.
 */
@Canonical
class UserController implements IController {

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
                return JsonUtils.dataToPrettyJson(service.findUserByName(username))
            } else {
                return JsonUtils.dataToPrettyJson(service.users)
            }
        });
    }

}
