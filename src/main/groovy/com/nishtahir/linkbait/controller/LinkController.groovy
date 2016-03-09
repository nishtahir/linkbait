package com.nishtahir.linkbait.controller

import com.nishtahir.linkbait.service.LinkService
import com.nishtahir.linkbait.util.JSONUtils
import groovy.transform.Canonical

import static spark.Spark.get

/**
 * Created by nish on 2/20/16.
 */
@Canonical
class LinkController extends AbstractController{

    LinkService service

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

        /**
         *  Returns the top links from the most active categories
         */
        get("/digest", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedThisMonth)
        });
    }
}
