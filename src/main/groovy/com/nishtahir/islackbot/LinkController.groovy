package com.nishtahir.islackbot

import com.nishtahir.islackbot.util.JSONUtils
import groovy.transform.Canonical

import static spark.Spark.get

/**
 * Created by nish on 2/20/16.
 */
@Canonical
class LinkController {

    LinkService service



    def init(){
        get("/today", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.getLinksPostedToday())
        });
    }
}
