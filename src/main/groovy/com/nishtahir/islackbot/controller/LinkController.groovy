package com.nishtahir.islackbot.controller

import com.nishtahir.islackbot.service.LinkService
import com.nishtahir.islackbot.util.JSONUtils
import groovy.transform.Canonical

import static spark.Spark.get

/**
 * Created by nish on 2/20/16.
 */
@Canonical
class LinkController extends AbstractController{

    LinkService service

    /**
     *
     */
    @Override
    void init(){

        /**
         *  Returns JSON with List of links posted today.
         */
        get("/today", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedToday)
        });

        /**
         *  Returns JSON with List of links posted this week.
         */
        get("/week", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedThisWeek)
        });

        /**
         *  Returns JSON with List of links posted this month.
         */
        get("/month", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedThisMonth)
        });
    }
}
